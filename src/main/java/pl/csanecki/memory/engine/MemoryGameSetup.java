package pl.csanecki.memory.engine;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public record MemoryGameSetup(Set<GroupToGuess> groupsToGuesses) {

    public List<FlatItemId> getAllFlatItemIds() {
        return groupsToGuesses.stream()
                .map(GroupToGuess::flatItemIds)
                .flatMap(Collection::stream)
                .toList();
    }

    public List<FlatItemsGroupId> getAllFlatItemsGroupIds() {
        return groupsToGuesses.stream()
                .map(GroupToGuess::flatItemsGroupId)
                .toList();
    }

    public record GroupToGuess(FlatItemsGroupId flatItemsGroupId, Set<FlatItemId> flatItemIds) {
    }
}
