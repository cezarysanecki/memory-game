package pl.csanecki.memory.engine;

import pl.csanecki.memory.engine.state.MemoryGameCurrentState;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.csanecki.memory.engine.GuessResult.Continue;
import static pl.csanecki.memory.engine.GuessResult.Failure;
import static pl.csanecki.memory.engine.GuessResult.GameOver;
import static pl.csanecki.memory.engine.GuessResult.Guessed;

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

        int numberOfGroups = numberOfCards / cardsInGroup;

        int itemId = 0;
        Set<FlatItemsGroup> flatItemsGroups = new HashSet<>();
        for (int i = 0; i < numberOfGroups; i++) {
            FlatItemsGroupId flatItemsGroupId = FlatItemsGroupId.of(i);

            Set<FlatItemId> flatItemIds = new HashSet<>();
            for (int j = 0; j < cardsInGroup; j++) {
                FlatItemId flatItemId = FlatItemId.of(itemId++);
                flatItemIds.add(flatItemId);
            }
            flatItemsGroups.add(FlatItemsGroup.allReversed(flatItemsGroupId, flatItemIds));
        }
        return new MemoryGame(flatItemsGroups);
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
