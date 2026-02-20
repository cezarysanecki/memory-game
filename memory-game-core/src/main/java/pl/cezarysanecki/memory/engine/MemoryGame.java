package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.cezarysanecki.memory.engine.FlatItem.Side.Obverse;
import static pl.cezarysanecki.memory.engine.FlatItem.Side.Reverse;

record MemoryGame(
        MemoryGameId memoryGameId,
        Set<FlatItemsGroup> groups,
        Set<FlatItemsGroup> guessed,
        FlatItemsGroup currentFlatGroupItem
) {

    static MemoryGame restore(Collection<MemoryGameEvent> events) {
        MemoryGameEvent.Initialized initializedEvent = events.stream()
                .filter(event -> event instanceof MemoryGameEvent.Initialized)
                .map(MemoryGameEvent.Initialized.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("cannot restore memory game without initialization event"));

        Set<FlatItemsGroup> flatItemsGroups = events.stream()
                .flatMap(event -> event.events().stream())
                .collect(Collectors.groupingBy(FlatItemsGroupEvent::flatItemsGroupId))
                .values().stream()
                .map(FlatItemsGroup::restore)
                .collect(Collectors.toUnmodifiableSet());

        Set<FlatItemsGroup> guessed = flatItemsGroups.stream()
                .filter(flatItemsGroup -> flatItemsGroup.turnUpAllTo(Obverse).isEmpty())
                .collect(Collectors.toUnmodifiableSet());

        FlatItemsGroup currentFlatItemsGroup = flatItemsGroups.stream()
                .filter(flatItemsGroup ->
                        flatItemsGroup.turnUpAllTo(Obverse).isPresent() && flatItemsGroup.turnUpAllTo(Reverse).isPresent()
                )
                .findFirst()
                .orElse(null);

        return new MemoryGame(initializedEvent.memoryGameId(), flatItemsGroups, guessed, currentFlatItemsGroup);
    }

    static MemoryGameEvent createNewOne(int numberOfCards, int cardsInGroup) {
        if (numberOfCards <= 0 || cardsInGroup <= 0) {
            throw new IllegalArgumentException("arguments must be positive");
        }
        if (numberOfCards % cardsInGroup != 0) {
            throw new IllegalArgumentException("number of cards must be dividable by cards in group");
        }

        int numberOfGroups = numberOfCards / cardsInGroup;

        List<FlatItemsGroupEvent> events = IntStream.range(0, numberOfGroups)
                .mapToObj(anything -> FlatItemsGroup.create(cardsInGroup, Reverse))
                .toList();

        return new MemoryGameEvent.Initialized(MemoryGameId.create(), events);
    }

    Optional<MemoryGameEvent> turnCard(FlatItemId flatItemId) {
        if (isAllGuessed()) {
            return Optional.empty();
        }

        return groups.stream()
                .map(group -> group.turnUpTo(flatItemId, Obverse))
                .flatMap(Optional::stream)
                .findFirst()
                .map(event -> {
                    if (currentFlatGroupItem == null) {
                        return new MemoryGameEvent.Continued(memoryGameId, List.of(event));
                    }
                    if (!currentFlatGroupItem.flatItemsGroupId().equals(event.flatItemsGroupId())) {
                        Optional<FlatItemsGroupEvent> revertingEvent = currentFlatGroupItem.turnUpAllTo(Reverse);

                        return new MemoryGameEvent.Missed(memoryGameId, revertingEvent.stream().toList());
                    }


                    if (event instanceof FlatItemsGroupEvent.AllTurnedObverse) {
                        List<FlatItemsGroupEvent> events = groups.stream()
                                .filter(group -> !group.flatItemsGroupId().equals(event.flatItemsGroupId()))
                                .map(group -> group.turnUpAllTo(Obverse))
                                .flatMap(Optional::stream)
                                .toList();

                        if (events.isEmpty()) {
                            return new MemoryGameEvent.Finished(memoryGameId, List.of(event));
                        }
                        return new MemoryGameEvent.Guessed(memoryGameId, List.of(event));
                    }
                    return new MemoryGameEvent.Continued(memoryGameId, List.of(event));
                });
    }

    private boolean isAllGuessed() {
        return guessed.containsAll(groups);
    }

}
