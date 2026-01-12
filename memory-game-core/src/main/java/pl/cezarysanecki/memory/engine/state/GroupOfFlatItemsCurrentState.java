package pl.cezarysanecki.memory.engine.state;

import java.util.Set;

public record GroupOfFlatItemsCurrentState(Set<FlatItemCurrentState> flatItems) {
}
