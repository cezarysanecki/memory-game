package pl.csanecki.memory.engine;

import pl.csanecki.memory.state.GroupOfFlatItemsCurrentState;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupOfFlatItems {

    private final Set<FlatItem> flatItems;

    private GroupOfFlatItems(Set<FlatItemId> flatItemIds, Function<FlatItemId, FlatItem> creator) {
        if (flatItemIds.isEmpty()) {
            throw new IllegalStateException("group of flat items cannot be empty");
        }
        this.flatItems = flatItemIds.stream()
                .map(creator)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static GroupOfFlatItems allReversed(Set<FlatItemId> flatItemIds) {
        return new GroupOfFlatItems(flatItemIds, FlatItem::reverseUp);
    }

    public void turnAllToReverseUp() {
        flatItems.forEach(FlatItem::turnReverseUp);
    }

    public void turnToAverse(FlatItemId flatItemId) {
        FlatItem flatItem = flatItems.stream()
                .filter(item -> item.getFlatItemId().equals(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("flat item " + flatItemId + " does not belong to group"));
        flatItem.turnAverseUp();
    }

    public boolean isAllReverseUp() {
        return flatItems.stream()
                .allMatch(FlatItem::isReverseUp);
    }

    public boolean isAllAverseUp() {
        return flatItems.stream()
                .allMatch(FlatItem::isAverseUp);
    }

    public boolean contains(FlatItemId flatItemId) {
        return flatItems.stream()
                .anyMatch(flatItem -> flatItem.getFlatItemId().equals(flatItemId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupOfFlatItems that = (GroupOfFlatItems) o;
        return Objects.equals(flatItems, that.flatItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatItems);
    }

    public GroupOfFlatItemsCurrentState currentState() {
        return new GroupOfFlatItemsCurrentState(flatItems.stream()
                .map(FlatItem::currentState)
                .collect(Collectors.toUnmodifiableSet()));
    }
}
