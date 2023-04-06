package pl.csanecki.memory;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupOfFlatItems {

    private final Set<FlatItem> flatItems;

    private GroupOfFlatItems(Set<FlatItemId> flatItemIds, Function<FlatItemId, FlatItem> creator) {
        if (flatItemIds.size() < 2) {
            throw new IllegalStateException("group of flat items needs at least two items");
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
        flatItems.stream()
                .filter(flatItem -> flatItem.getFlatItemId().equals(flatItemId))
                .filter(FlatItem::isReverseUp)
                .findFirst()
                .ifPresent(FlatItem::turnAverseUp);
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

}
