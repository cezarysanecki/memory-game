package pl.cezarysanecki.memory.engine.state;

import java.util.Set;

public record MemoryGameCurrentState(Set<GroupOfFlatItemsCurrentState> groupOfFlatItems, boolean isFinished) {
}
