package pl.csanecki.memory;

import java.util.Set;

import static pl.csanecki.memory.GuessResult.*;

public class MemoryGame {

    private final Set<GroupOfFlatItems> groups;
    private GroupOfFlatItems current = null;

    public MemoryGame(Set<GroupOfFlatItems> groups) {
        this.groups = groups;
    }

    public GuessResult turnCard(FlatItemId flatItemId) {
        if (current == null) {
            current = findBy(flatItemId);
        } else {
            GroupOfFlatItems pretender = findBy(flatItemId);
            if (!pretender.equals(current)) {
                return Failure;
            }
        }

        current.turnToAverse(flatItemId);

        return current.isAllAverseUp() ? Guessed : Continue;
    }

    public GroupOfFlatItems findBy(FlatItemId flatItemId) {
        return groups.stream()
                .filter(group -> group.contains(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("cannot find group for flat item: " + flatItemId));
    }
}