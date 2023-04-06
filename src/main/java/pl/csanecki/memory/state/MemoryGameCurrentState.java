package pl.csanecki.memory.state;

import java.util.Set;

public record MemoryGameCurrentState(Set<GroupOfFlatItemsCurrentState> groupOfFlatItems, boolean isFinished) {
}
