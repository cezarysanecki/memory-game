package pl.csanecki.memory.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    private final long millisecondsRefresh;

    private ScheduledExecutorService timer;
    private long passed;

    private Timer(long millisecondsRefresh) {
        if (millisecondsRefresh <= 0) {
            throw new IllegalStateException("value of milliseconds must be positive");
        }
        this.millisecondsRefresh = millisecondsRefresh;
    }

    public static Timer ofTenMilliseconds() {
        return new Timer(10);
    }

    public static Timer ofOneHundredMilliseconds() {
        return new Timer(100);
    }

    public static Timer ofOneThousandMilliseconds() {
        return new Timer(1_000);
    }

    public void start() {
        passed = 0;
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(() -> passed += millisecondsRefresh, 0, millisecondsRefresh, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        timer.shutdown();
    }

    public long getResultAsMilliseconds() {
        return passed;
    }

    public double getResultAsSeconds() {
        return (double) passed / 1000;
    }

}
