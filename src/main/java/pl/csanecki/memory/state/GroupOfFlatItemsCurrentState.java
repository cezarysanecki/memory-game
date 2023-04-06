package pl.csanecki.memory.state;

import java.util.Set;

public record GroupOfFlatItemsCurrentState(Set<FlatItemCurrentState> flatItems) {
}
