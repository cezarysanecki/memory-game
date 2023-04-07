package pl.csanecki.memory.util;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MillisTimer {

    private final MillisRefreshment millisRefreshment;
    private Collection<MillisTimerSubscriber> millisTimerSubscribers;

    private ScheduledExecutorService timer;
    private long passed;

    private MillisTimer(MillisRefreshment millisRefreshment) {
        this.millisRefreshment = millisRefreshment;
    }

    public static MillisTimer ofTenMilliseconds() {
        return new MillisTimer(MillisRefreshment.Ten);
    }

    public static MillisTimer ofOneHundredMilliseconds() {
        return new MillisTimer(MillisRefreshment.OneHundred);
    }

    public static MillisTimer ofOneThousandMilliseconds() {
        return new MillisTimer(MillisRefreshment.OneThousand);
    }

    public void registerSubscribers(Collection<MillisTimerSubscriber> millisTimerSubscribers) {
        this.millisTimerSubscribers = millisTimerSubscribers;
    }

    public void start() {
        passed = 0;
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(() -> {
            passed += millisRefreshment.millis;
            millisTimerSubscribers.forEach(millisTimerSubscriber -> millisTimerSubscriber.update(getResultAsMilliseconds(), millisRefreshment));
        }, 0, millisRefreshment.millis, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        timer.shutdown();
    }

    public Duration getResultAsMilliseconds() {
        return Duration.ofMillis(passed);
    }

}
