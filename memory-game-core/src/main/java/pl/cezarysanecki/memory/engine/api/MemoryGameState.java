package pl.cezarysanecki.memory.engine.api;

import pl.cezarysanecki.memory.engine.FlatItemEvent;
import pl.cezarysanecki.memory.engine.MemoryGameEvent;

import java.util.Set;
import java.util.stream.Collectors;

public record MemoryGameState(
        MemoryGameId memoryGameId,
        Set<FlatItem> flatItems,
        boolean ended
) {

    public static MemoryGameState initialize(MemoryGameEvent.Initialized event) {
        return new MemoryGameState(
                event.memoryGameId(),
                event.events().stream()
                        .flatMap(flatItemsGroupEvent -> flatItemsGroupEvent.events().stream()
                                .filter(flatItemEvent -> flatItemEvent instanceof FlatItemEvent.Initialized)
                                .map(flatItemInitialized -> new FlatItem(
                                        flatItemInitialized.flatItemId(),
                                        flatItemsGroupEvent.flatItemsGroupId(),
                                        false
                                ))
                        )
                        .collect(Collectors.toUnmodifiableSet()),
                false
        );
    }

    public MemoryGameState markAsFinished() {
        return new MemoryGameState(memoryGameId, flatItems, true);
    }

    public record FlatItem(
            FlatItemId flatItemId,
            FlatItemsGroupId assignedGroupId,
            boolean obverseUp
    ) {
    }

    public MemoryGameState evolve(FlatItemEvent event) {
        if (event instanceof FlatItemEvent.TurnedObverseUp) {
            return new MemoryGameState(
                    memoryGameId,
                    flatItems.stream()
                            .map(flatItem -> {
                                if (flatItem.flatItemId().equals(event.flatItemId())) {
                                    return new FlatItem(
                                            flatItem.flatItemId(),
                                            flatItem.assignedGroupId(),
                                            true
                                    );
                                } else {
                                    return flatItem;
                                }
                            })
                            .collect(Collectors.toUnmodifiableSet()),
                    ended
            );
        } else if (event instanceof FlatItemEvent.TurnedReverseUp) {
            return new MemoryGameState(
                    memoryGameId,
                    flatItems.stream()
                            .map(flatItem -> {
                                if (flatItem.flatItemId().equals(event.flatItemId())) {
                                    return new FlatItem(
                                            flatItem.flatItemId(),
                                            flatItem.assignedGroupId(),
                                            false
                                    );
                                } else {
                                    return flatItem;
                                }
                            })
                            .collect(Collectors.toUnmodifiableSet()),
                    ended
            );
        } else {
            return this;
        }
    }

}
