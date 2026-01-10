package pl.csanecki.memory.engine;

import java.util.Set;

public record MemoryGameState(
        MemoryGameId memoryGameId,
        Set<FlatItem> flatItemStates
) {

    public record FlatItem(
            FlatItemId flatItemId,
            FlatItemsGroupId flatItemsGroupId,
            boolean obverseUp
    ) {

    }

}
