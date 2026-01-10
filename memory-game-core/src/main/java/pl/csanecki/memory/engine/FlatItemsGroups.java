package pl.csanecki.memory.engine;

import java.util.Set;
import java.util.stream.Collectors;

public final class FlatItemsGroups {

    public final Set<FlatItemsGroup> groups;

    public FlatItemsGroups(Set<FlatItemsGroup> groups) {
        this.groups = groups;
    }

    public FlatItemsGroupId findGroupRelatedTo(FlatItemId flatItemId) {
        return groups.stream()
                .filter(group -> group.contains(flatItemId))
                .findFirst()
                .map(flatItemsGroup -> flatItemsGroup.flatItemsGroupId)
                .orElseThrow(() -> new IllegalArgumentException("cannot find group for flat item: " + flatItemId));
    }

    public Set<FlatItemsGroupId> findGroupsWithMixedSides() {
        return groups.stream()
                .filter(group -> !group.isAllObverseUp() && !group.isAllReverseUp())
                .map(flatItemsGroup -> flatItemsGroup.flatItemsGroupId)
                .collect(Collectors.toUnmodifiableSet());
    }

    public boolean isAllObverseUp() {
        return groups.stream()
                .allMatch(FlatItemsGroup::isAllObverseUp);
    }
}
