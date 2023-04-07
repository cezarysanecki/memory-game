package pl.csanecki.memory.util;

import java.time.Duration;

public interface MillisTimerSubscriber {

    void update(Duration passed);

}
