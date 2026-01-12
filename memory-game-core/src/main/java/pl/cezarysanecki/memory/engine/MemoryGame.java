package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.GuessResult;

import java.util.Set;

import static pl.cezarysanecki.memory.engine.api.GuessResult.Continue;
import static pl.cezarysanecki.memory.engine.api.GuessResult.Failure;
import static pl.cezarysanecki.memory.engine.api.GuessResult.GameOver;
import static pl.cezarysanecki.memory.engine.api.GuessResult.Guessed;

class MemoryGame {

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

    GuessResult turnCard(FlatItemId flatItemId) {
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

    private boolean isAllGuessed() {
        return guessed.containsAll(groups);
    }

    private FlatItemsGroup findBy(FlatItemId flatItemId) {
        return groups.stream()
                .filter(group -> group.contains(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("cannot find group for flat item: " + flatItemId));
    }

}
