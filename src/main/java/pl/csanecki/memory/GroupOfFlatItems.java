package pl.csanecki.memory;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupOfFlatItems {

    private final Set<FlatItem> flatItems;

    private GroupOfFlatItems(Set<FlatItemId> flatItemIds, Function<FlatItemId, FlatItem> creator) {
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

    public Result turnToAverse(FlatItemId flatItemId) {
        return flatItems.stream()
                .filter(flatItem -> flatItem.getFlatItemId().equals(flatItemId))
                .filter(FlatItem::isReverseUp)
                .findFirst()
                .map(flatItem -> {
                    flatItem.turnAverseUp();
                    return Result.Success;
                })
                .orElse(Result.Failure);
    }

    public boolean isAllReverseUp() {
        return flatItems.stream()
                .allMatch(FlatItem::isReverseUp);
    }

    public boolean isAllAverseUp() {
        return flatItems.stream()
                .allMatch(FlatItem::isAverseUp);
    }

}
