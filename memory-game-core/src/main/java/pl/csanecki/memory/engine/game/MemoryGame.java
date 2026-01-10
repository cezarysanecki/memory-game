package pl.csanecki.memory.engine.game;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroup;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.engine.FlatItemsGroups;
import pl.csanecki.memory.engine.GuessResult;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.csanecki.memory.engine.GuessResult.Continue;
import static pl.csanecki.memory.engine.GuessResult.Failure;
import static pl.csanecki.memory.engine.GuessResult.GameOver;
import static pl.csanecki.memory.engine.GuessResult.Guessed;

public class MemoryGame {

    private final FlatItemsGroups groups;
    private FlatItemsGroupId searchedGroup;

    MemoryGame(FlatItemsGroups groups) {
        Set<FlatItemsGroupId> groupsWithMixedSides = groups.findGroupsWithMixedSides();
        if (groupsWithMixedSides.size() > 1) {
            throw new IllegalStateException("Cannot create game with more than one group with mixed sides");
        }

        this.groups = groups;
        this.searchedGroup = groupsWithMixedSides.stream().findFirst().orElse(null);
    }

    public GuessResult turnCard(FlatItemId flatItemId) {
        if (groups.isAllObverseUp()) {
            return GameOver;
        }

        if (searchedGroup == null) {
            searchedGroup = groups.findGroupRelatedTo(flatItemId);
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

}
