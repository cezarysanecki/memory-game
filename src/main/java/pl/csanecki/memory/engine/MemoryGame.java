package pl.csanecki.memory.engine;

import pl.csanecki.memory.state.MemoryGameCurrentState;
import pl.csanecki.memory.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.csanecki.memory.engine.GuessResult.*;

public class MemoryGame {

    private final Set<FlatItemsGroup> groups;

    private final Set<FlatItemsGroup> guessed = new HashSet<>();

    private FlatItemsGroup current = null;

    public MemoryGame(MemoryGameSetup memoryGameSetup) {
        List<FlatItemId> flatItemIds = memoryGameSetup.groupsToGuesses()
                .stream()
                .map(MemoryGameSetup.GroupToGuess::flatItemIds)
                .flatMap(Collection::stream)
                .toList();
        if (CollectionUtils.containsDuplicates(flatItemIds)) {
            throw new IllegalArgumentException("flat item ids must be unique");
        }

        this.groups = memoryGameSetup.groupsToGuesses()
                .stream()
                .map(groupToGuess -> FlatItemsGroup.allReversed(groupToGuess.flatItemIds()))
                .collect(Collectors.toUnmodifiableSet());
    }

    public void reset() {
        current = null;
        guessed.clear();
    }

    public GuessResult turnCard(FlatItemId flatItemId) {
        if (isAllGuessed()) {
            return GameOver;
        } else if (current == null) {
            current = findBy(flatItemId);
        } else if (!current.contains(flatItemId)) {
            current.turnAllToReverseUp();
            current = null;
            return Failure;
        }

        current.turnToAverse(flatItemId);

        if (current.isAllAverseUp()) {
            guessed.add(current);
            current = null;
            if (isAllGuessed()) {
                return GameOver;
            }
            return Guessed;
        }
        return Continue;
    }

    private boolean isAllGuessed() {
        return guessed.containsAll(groups);
    }

    private FlatItemsGroup findBy(FlatItemId flatItemId) {
        return groups.stream()
                .filter(group -> group.contains(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("cannot find group for flat item: " + flatItemId));
    }

    public MemoryGameCurrentState currentState() {
        return new MemoryGameCurrentState(
                groups.stream()
                        .map(FlatItemsGroup::currentState)
                        .collect(Collectors.toUnmodifiableSet()), isAllGuessed());
    }
}
