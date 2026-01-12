package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.GuessResult;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.cezarysanecki.memory.engine.api.GuessResult.State.Continue;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.Failure;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.GameOver;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.Guessed;

class MemoryGame {

    private final MemoryGameId memoryGameId;
    private final Set<FlatItemsGroup> groups;
    private final Set<FlatItemsGroup> guessed;
    private FlatItemsGroup current;

    MemoryGame(MemoryGameId memoryGameId, Set<FlatItemsGroup> groups) {
        this(memoryGameId, groups, new HashSet<>(), null);
    }

    MemoryGame(
            MemoryGameId memoryGameId,
            Set<FlatItemsGroup> groups,
            Set<FlatItemsGroup> guessed,
            FlatItemsGroup current
    ) {
        this.memoryGameId = memoryGameId;
        this.groups = groups;
        this.guessed = guessed;
        this.current = current;
    }

    GuessResult turnCard(FlatItemId flatItemId) {
        if (isAllGuessed()) {
            return new GuessResult(GameOver, state());
        } else if (current == null) {
            current = findBy(flatItemId);
        } else if (!current.contains(flatItemId)) {
            FlatItemsGroup different = findBy(flatItemId);
            if (different.isAllObverseUp()) {
                return new GuessResult(Continue, state());
            }
            current.turnAllToReverseUp();
            current = null;
            return new GuessResult(Failure, state());
        }

        current.turnToObverse(flatItemId);

        if (current.isAllObverseUp()) {
            guessed.add(current);
            current = null;
            if (isAllGuessed()) {
                return new GuessResult(GameOver, state());
            }
            return new GuessResult(Guessed, state());
        }
        return new GuessResult(Continue, state());
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

    MemoryGameState state() {
        return new MemoryGameState(
                memoryGameId,
                groups.stream()
                        .flatMap(group -> group.flatItems().stream()
                                .map(flatItem -> new MemoryGameState.FlatItem(
                                        flatItem.getFlatItemId(),
                                        group.flatItemsGroupId(),
                                        flatItem.isObverseUp()
                                ))
                        )
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

}
