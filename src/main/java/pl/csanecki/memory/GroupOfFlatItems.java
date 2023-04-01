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
        return new GroupOfFlatItems(flatItemIds, FlatItem::reverse);
    }

    public void turnAllToReverse() {
        flatItems.forEach(FlatItem::turnToReverse);
    }

    public Result turnToAverse(FlatItemId flatItemId) {
        return flatItems.stream()
                .filter(flatItem -> flatItem.getFlatItemId().equals(flatItemId))
                .findFirst()
                .map(flatItem -> {
                    flatItem.turnToAverse();
                    return Result.Success;
                })
                .orElse(Result.Failure);
    }

    public int count() {
        return flatItems.size();
    }


}
