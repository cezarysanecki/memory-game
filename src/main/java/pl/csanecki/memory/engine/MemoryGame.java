package pl.csanecki.memory.engine;

import pl.csanecki.memory.engine.state.MemoryGameCurrentState;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.csanecki.memory.engine.GuessResult.*;

public class MemoryGame {

    private final Set<FlatItemsGroup> groups;

    private final Set<FlatItemsGroup> guessed = new HashSet<>();

    private FlatItemsGroup current = null;

    private MemoryGame(Set<FlatItemsGroup> groups) {
        this.groups = groups;
    }

    public static MemoryGame create(int numberOfCards, int cardsInGroup) {
        if (numberOfCards % cardsInGroup != 0) {
            throw new IllegalArgumentException("number of cards must be dividable by cards in group");
        }
        if (numberOfCards <= 0 || cardsInGroup <= 0) {
            throw new IllegalArgumentException("arguments must be positive");
        }
        if (cardsInGroup > numberOfCards) {
            throw new IllegalArgumentException("number of cards must be grater than cards in group");
        }

        int numberOfGroups = numberOfCards / cardsInGroup;
        AtomicInteger flatItemGenerator = new AtomicInteger(0);

        Set<FlatItemsGroup> flatItemsGroups = IntStream.range(0, numberOfGroups)
                .mapToObj(FlatItemsGroupId::of)
                .map(flatItemsGroupId -> FlatItemsGroup.allReversed(
                        flatItemsGroupId,
                        IntStream.range(0, cardsInGroup)
                                .mapToObj(index -> FlatItemId.of(flatItemGenerator.getAndIncrement()))
                                .collect(Collectors.toUnmodifiableSet())))
                .collect(Collectors.toUnmodifiableSet());
        return new MemoryGame(flatItemsGroups);
    }

    public GuessResult turnCard(FlatItemId flatItemId) {
        if (isAllGuessed()) {
            return GameOver;
        } else if (current == null) {
            current = findBy(flatItemId);
        } else if (!current.contains(flatItemId)) {
            FlatItemsGroup different = findBy(flatItemId);
            if (different.isAllObverseUp()) {
                return Continue;
            }
            current.turnAllToReverseUp();
            current = null;
            return Failure;
        }

        current.turnToObverse(flatItemId);

        if (current.isAllObverseUp()) {
            guessed.add(current);
            current = null;
            if (isAllGuessed()) {
                return GameOver;
            }
            return Guessed;
        }
        return Continue;
    }

    public void reset() {
        current = null;
        guessed.clear();
        groups.forEach(FlatItemsGroup::turnAllToReverseUp);
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
