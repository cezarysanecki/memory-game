package pl.csanecki.memory.engine;

import java.util.Optional;
import java.util.Set;

public interface MemoryGameState {

    void create(Set<FlatItemsGroup> groups);

    void storeGuessed(FlatItemsGroup flatItemsGroup);

    FlatItemsGroup findBy(FlatItemId flatItemId);

    void setCurrent(FlatItemsGroup flatItemsGroup);

    Optional<FlatItemsGroup> current();

    boolean isAllGuessed();

}
