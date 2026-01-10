package pl.csanecki.memory.engine;

import pl.csanecki.memory.engine.state.MemoryGameCurrentState;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.csanecki.memory.engine.GuessResult.Continue;
import static pl.csanecki.memory.engine.GuessResult.Failure;
import static pl.csanecki.memory.engine.GuessResult.GameOver;
import static pl.csanecki.memory.engine.GuessResult.Guessed;

public class MemoryGame {

    private final Set<FlatItemsGroup> groups;

    private final Set<FlatItemsGroup> guessed = new HashSet<>();

    private FlatItemsGroup searchedGroup = null;

    private MemoryGame(Set<FlatItemsGroup> groups) {
        this.groups = groups;
    }

    public static MemoryGame create(int numberOfCards, int cardsInGroup) {
        if (numberOfCards <= 0 || cardsInGroup <= 0) {
            throw new IllegalArgumentException("arguments must be positive");
        }
        if (numberOfCards % cardsInGroup != 0) {
            throw new IllegalArgumentException("number of cards must be dividable by cards in group");
        }

        int numberOfGroups = numberOfCards / cardsInGroup;
        AtomicInteger flatItemIdSequenceGenerator = new AtomicInteger(0);

        Set<FlatItemsGroup> flatItemsGroups = IntStream.range(0, numberOfGroups)
                .mapToObj(FlatItemsGroupId::of)
                .map(flatItemsGroupId -> FlatItemsGroup.allReversed(
                        flatItemsGroupId,
                        IntStream.range(0, cardsInGroup)
                                .mapToObj(index -> FlatItemId.of(flatItemIdSequenceGenerator.getAndIncrement()))
                                .collect(Collectors.toUnmodifiableSet())))
                .collect(Collectors.toUnmodifiableSet());
        return new MemoryGame(flatItemsGroups);
    }

    public GuessResult turnCard(FlatItemId flatItemId) {
        if (isAllGuessed()) {
            return GameOver;
        }

        if (searchedGroup == null) {
            searchedGroup = findGroupBy(flatItemId);
        } else if (!searchedGroup.contains(flatItemId)) {
            FlatItemsGroup different = findGroupBy(flatItemId);
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

    public void reset() {
        searchedGroup = null;
        guessed.clear();
        groups.forEach(FlatItemsGroup::turnAllToReverseUp);
    }

    private boolean isAllGuessed() {
        return guessed.containsAll(groups);
    }

    private FlatItemsGroup findGroupBy(FlatItemId flatItemId) {
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
