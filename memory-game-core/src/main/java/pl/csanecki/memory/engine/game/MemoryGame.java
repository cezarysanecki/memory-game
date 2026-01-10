package pl.csanecki.memory.engine.game;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroup;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.engine.GuessResult;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.csanecki.memory.engine.GuessResult.Continue;
import static pl.csanecki.memory.engine.GuessResult.Failure;
import static pl.csanecki.memory.engine.GuessResult.GameOver;
import static pl.csanecki.memory.engine.GuessResult.Guessed;

public class MemoryGame {

    private final Set<FlatItemsGroup> groups;
    private final Set<FlatItemsGroupId> guessed;
    private FlatItemsGroupId searchedGroup;

    MemoryGame(Set<FlatItemsGroup> groups) {
        this.groups = groups;
        this.guessed = new HashSet<>();
        this.searchedGroup = null;
    }

    MemoryGame(Set<FlatItemsGroup> groups, Set<FlatItemsGroupId> guessed, FlatItemsGroupId searchedGroup) {
        this.groups = groups;
        this.guessed = guessed;
        this.searchedGroup = searchedGroup;
    }

    public GuessResult turnCard(FlatItemId flatItemId) {
        if (isAllGuessed()) {
            return GameOver;
        }

        if (searchedGroup == null) {
            searchedGroup = findGroupRelatedTo(flatItemId);
        } else if (!searchedGroup.contains(flatItemId)) {
            FlatItemsGroup different = findGroupRelatedTo(flatItemId);
            if (different.isAllObverseUp()) {
                return Continue;
            }
            searchedGroup.turnAllToReverseUp();
            searchedGroup = null;
            return Failure;
        }

        searchedGroup.turnObverseUp(flatItemId);

        if (searchedGroup.isAllObverseUp()) {
            guessed.add(searchedGroup);

            searchedGroup = null;
            if (isAllGuessed()) {
                return GameOver;
            }
            return Guessed;
        }

        return Continue;
    }

    private boolean isAllGuessed() {
        Set<FlatItemsGroupId> allFlatItemGroupIds = groups.stream()
                .map(group -> group.flatItemsGroupId)
                .collect(Collectors.toSet());
        return allFlatItemGroupIds.containsAll(guessed);
    }

    private FlatItemsGroupId findGroupRelatedTo(FlatItemId flatItemId) {
        return groups.stream()
                .filter(group -> group.contains(flatItemId))
                .findFirst()
                .map(flatItemsGroup -> flatItemsGroup.flatItemsGroupId)
                .orElseThrow(() -> new IllegalArgumentException("cannot find group for flat item: " + flatItemId));
    }

}
