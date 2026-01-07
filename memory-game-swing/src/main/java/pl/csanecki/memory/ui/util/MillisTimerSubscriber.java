package pl.csanecki.memory.ui.util;

import java.time.Duration;

public interface MillisTimerSubscriber {

    void update(Duration passed, MillisRefreshment millisRefreshment);

}
