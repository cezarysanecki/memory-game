package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.FlatItemsGroupEvent.AllTurnedObverse;
import pl.cezarysanecki.memory.engine.FlatItemsGroupEvent.AllTurnedReverse;
import pl.cezarysanecki.memory.engine.FlatItemsGroupEvent.FlatItemTurned;
import pl.cezarysanecki.memory.engine.FlatItemsGroupEvent.Initialized;
import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

record FlatItemsGroup(
        FlatItemsGroupId flatItemsGroupId,
        Set<FlatItem> flatItems
) {

    FlatItemsGroup {
        if (flatItems.isEmpty()) {
            throw new IllegalStateException("group of flat items cannot be empty");
        }
    }

    static FlatItemsGroup restore(
            List<FlatItemsGroupEvent> events
    ) {
        FlatItemsGroupEvent.Initialized initializedEvent = events.stream()
                .filter(event -> event instanceof FlatItemsGroupEvent.Initialized)
                .map(FlatItemsGroupEvent.Initialized.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("cannot restore flat item group without initialization event"));

        Set<FlatItem> restoredItems = events.stream()
                .flatMap(event -> event.events().stream())
                .collect(Collectors.groupingBy(FlatItemEvent::flatItemId))
                .values()
                .stream()
                .map(FlatItem::restore)
                .collect(Collectors.toUnmodifiableSet());

        return new FlatItemsGroup(initializedEvent.flatItemsGroupId(), restoredItems);
    }

    static FlatItemsGroupEvent create(int numberOfFlatItems, FlatItem.Side side) {
        if (numberOfFlatItems <= 0) {
            throw new IllegalArgumentException("number of flat items must be positive");
        }

        List<FlatItemEvent> flatItemEvents = IntStream.range(0, numberOfFlatItems)
                .mapToObj(initialFlatItem -> FlatItem.create(side))
                .flatMap(List::stream)
                .toList();

        return new Initialized(FlatItemsGroupId.create(), flatItemEvents);
    }

    Optional<FlatItemsGroupEvent> turnUpAllTo(FlatItem.Side side) {
        List<FlatItemEvent> events = flatItems.stream()
                .map(flatItem -> flatItem.turnUpTo(side))
                .flatMap(Optional::stream)
                .toList();
        if (events.isEmpty()) {
            return Optional.empty();
        }

        return switch (side) {
            case Obverse -> Optional.of(new AllTurnedObverse(flatItemsGroupId, events));
            case Reverse -> Optional.of(new AllTurnedReverse(flatItemsGroupId, events));
        };
    }

    Optional<FlatItemsGroupEvent> turnUpTo(FlatItemId flatItemId, FlatItem.Side side) {
        return flatItems.stream()
                .map(flatItem -> flatItem.turnUpTo(side))
                .flatMap(Optional::stream)
                .filter(event -> event.flatItemId().equals(flatItemId))
                .findFirst()
                .map(event -> {
                    List<FlatItemEvent> events = flatItems.stream()
                            .filter(flatItem -> !flatItem.flatItemId().equals(flatItemId))
                            .map(flatItem -> flatItem.turnUpTo(side))
                            .flatMap(Optional::stream)
                            .toList();

                    if (events.isEmpty()) {
                        return switch (side) {
                            case Obverse -> new AllTurnedObverse(flatItemsGroupId, List.of(event));
                            case Reverse -> new AllTurnedReverse(flatItemsGroupId, List.of(event));
                        };
                    }
                    return new FlatItemTurned(flatItemsGroupId, List.of(event));
                });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatItemsGroup that = (FlatItemsGroup) o;
        return Objects.equals(flatItemsGroupId, that.flatItemsGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatItemsGroupId);
    }

}
