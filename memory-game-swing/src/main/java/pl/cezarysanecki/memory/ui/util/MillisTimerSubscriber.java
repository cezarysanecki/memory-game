package pl.cezarysanecki.memory.util;

import java.time.Duration;

public interface MillisTimerSubscriber {

    void update(Duration passed, MillisRefreshment millisRefreshment);

}
