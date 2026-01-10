package pl.csanecki.memory.engine;

import pl.csanecki.memory.engine.state.GroupOfFlatItemsCurrentState;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class FlatItemsGroup {

    public final FlatItemsGroupId flatItemsGroupId;
    public final Set<FlatItem> flatItems;

    private FlatItemsGroup(FlatItemsGroupId flatItemsGroupId, Set<FlatItemId> flatItemIds, Function<FlatItemId, FlatItem> creator) {
        if (flatItemIds.isEmpty()) {
            throw new IllegalStateException("group of flat items cannot be empty");
        }

        this.flatItemsGroupId = flatItemsGroupId;
        this.flatItems = flatItemIds.stream()
                .map(creator)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static FlatItemsGroup allReversed(FlatItemsGroupId flatItemsGroupId, Set<FlatItemId> flatItemIds) {
        return new FlatItemsGroup(flatItemsGroupId, flatItemIds, FlatItem::reverseUp);
    }

    public void turnAllToReverseUp() {
        flatItems.forEach(FlatItem::turnReverseUp);
    }

    public void turnObverseUp(FlatItemId flatItemId) {
        FlatItem flatItem = flatItems.stream()
                .filter(item -> item.getFlatItemId().equals(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("flat item " + flatItemId + " does not belong to group"));

        flatItem.turnObverseUp();
    }

    public boolean contains(FlatItemId flatItemId) {
        return flatItems.stream()
                .anyMatch(flatItem -> flatItem.getFlatItemId().equals(flatItemId));
    }

    public boolean isAllReverseUp() {
        return flatItems.stream()
                .allMatch(FlatItem::isReverseUp);
    }

    public boolean isAllObverseUp() {
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
