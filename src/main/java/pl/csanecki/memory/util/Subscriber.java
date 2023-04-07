package pl.csanecki.memory.util;

import java.time.Duration;

public interface Subscriber {

    void update(Duration passed);

}
