package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.state.MemoryGameCurrentState;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.cezarysanecki.memory.engine.GuessResult.*;

public class MemoryGame {

    private Set<FlatItemsGroup> groups;
    private Set<FlatItemsGroup> guessed;
    private FlatItemsGroup current;

    MemoryGame(
            Set<FlatItemsGroup> groups,
            Set<FlatItemsGroup> guessed,
            FlatItemsGroup current
    ) {
        this.groups = groups;
        this.guessed = guessed;
        this.current = current;
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
