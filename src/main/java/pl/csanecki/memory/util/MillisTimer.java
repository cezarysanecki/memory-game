package pl.csanecki.memory.util;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MillisTimer {

    private final long millisecondsRefresh;
    private Collection<MillisTimerSubscriber> millisTimerSubscribers;

    private ScheduledExecutorService timer;
    private long passed;

    private MillisTimer(long millisRefreshment) {
        if (millisRefreshment <= 0) {
            throw new IllegalStateException("value of milliseconds must be positive");
        }
        this.millisecondsRefresh = millisRefreshment;
    }

    public static MillisTimer ofTenMilliseconds() {
        return new MillisTimer(10);
    }

    public static MillisTimer ofOneHundredMilliseconds() {
        return new MillisTimer(100);
    }

    public static MillisTimer ofOneThousandMilliseconds() {
        return new MillisTimer(1_000);
    }

    public void registerSubscribers(Collection<MillisTimerSubscriber> millisTimerSubscribers) {
        this.millisTimerSubscribers = millisTimerSubscribers;
    }

    public void start() {
        passed = 0;
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(() -> {
            passed += millisecondsRefresh;
            millisTimerSubscribers.forEach(millisTimerSubscriber -> millisTimerSubscriber.update(getResultAsMilliseconds()));
        }, 0, millisecondsRefresh, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        timer.shutdown();
    }

    public Duration getResultAsMilliseconds() {
        return Duration.ofMillis(passed);
    }

}
