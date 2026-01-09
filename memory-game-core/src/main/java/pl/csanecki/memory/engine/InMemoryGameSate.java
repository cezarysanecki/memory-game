package pl.csanecki.memory.engine;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InMemoryGameSate implements MemoryGameState {

    private static Set<FlatItemsGroup> GROUPS;

    private static Set<FlatItemsGroup> GUESSED = new HashSet<>();

    private static FlatItemsGroup CURRENT = null;

    public InMemoryGameSate(Set<FlatItemsGroup> groups) {
        create(groups);
    }

    @Override
    public void create(Set<FlatItemsGroup> groups) {
        GROUPS = groups;
        GUESSED = new HashSet<>();
        CURRENT = null;
    }

    @Override
    public void storeGuessed(FlatItemsGroup flatItemsGroup) {
        GUESSED.add(flatItemsGroup);
    }

    @Override
    public FlatItemsGroup findBy(FlatItemId flatItemId) {
        return GROUPS.stream()
                .filter(group -> group.contains(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("cannot find group for flat item: " + flatItemId));
    }

    @Override
    public void setCurrent(FlatItemsGroup flatItemsGroup) {
        CURRENT = flatItemsGroup;
    }

    @Override
    public Optional<FlatItemsGroup> current() {
        return Optional.of(CURRENT);
    }

    @Override
    public boolean isAllGuessed() {
        return GUESSED.containsAll(GROUPS);
    }
}
