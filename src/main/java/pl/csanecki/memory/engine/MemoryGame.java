package pl.csanecki.memory.engine;

import java.util.HashSet;
import java.util.Set;

import static pl.csanecki.memory.engine.GuessResult.*;

public class MemoryGame {

    private final Set<GroupOfFlatItems> groups;

    private GroupOfFlatItems current = null;
    private Set<GroupOfFlatItems> guessed = new HashSet<>();

    public MemoryGame(Set<GroupOfFlatItems> groups) {
        this.groups = groups;
    }

    public void reset() {
        current = null;
        guessed.clear();
    }

    public GuessResult turnCard(FlatItemId flatItemId) {
        if (guessed.containsAll(groups)) {
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
            if (guessed.containsAll(groups)) {
                return GameOver;
            }
            return Guessed;
        }
        return Continue;
    }

    private GroupOfFlatItems findBy(FlatItemId flatItemId) {
        return groups.stream()
                .filter(group -> group.contains(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("cannot find group for flat item: " + flatItemId));
    }
}
