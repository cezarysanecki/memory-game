package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

record FlatItemsGroup(
        FlatItemsGroupId flatItemsGroupId,
        Set<FlatItem> flatItems
) {

    FlatItemsGroup {
        if (flatItems.isEmpty()) {
            throw new IllegalStateException("group of flat items cannot be empty");
        }
    }

    static FlatItemsGroup create(
            FlatItemsGroupId flatItemsGroupId,
            Set<FlatItemId> flatItemIds,
            Function<FlatItemId, FlatItem> creator
    ) {
        return new FlatItemsGroup(
                flatItemsGroupId,
                flatItemIds.stream()
                        .map(creator)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    static FlatItemsGroup allReversed(FlatItemsGroupId flatItemsGroupId, Set<FlatItemId> flatItemIds) {
        return create(
                flatItemsGroupId,
                flatItemIds,
                FlatItem::reverseUp
        );
    }

    void turnAllToReverseUp() {
        flatItems.forEach(FlatItem::turnReverseUp);
    }

    void turnToObverse(FlatItemId flatItemId) {
        FlatItem flatItem = flatItems.stream()
                .filter(item -> item.getFlatItemId().equals(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("flat item " + flatItemId + " does not belong to group"));
        flatItem.turnObverseUp();
    }

    boolean contains(FlatItemId flatItemId) {
        return flatItems.stream()
                .anyMatch(flatItem -> flatItem.getFlatItemId().equals(flatItemId));
    }

    boolean isAllReverseUp() {
        return flatItems.stream()
                .allMatch(FlatItem::isReverseUp);
    }

    boolean isAllObverseUp() {
        return flatItems.stream()
                .allMatch(FlatItem::isObverseUp);
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
