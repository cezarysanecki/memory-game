package pl.cezarysanecki.memory.engine.api;

import java.util.Set;

public record MemoryGameView(
        MemoryGameId memoryGameId,
        Set<FlatItem> flatItems
) {
    public record FlatItem(
            FlatItemId flatItemId,
            FlatItemsGroupId assignedGroupId,
            boolean obverseUp
    ) {
    }
}
