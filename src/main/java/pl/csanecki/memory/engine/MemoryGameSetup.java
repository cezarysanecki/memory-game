package pl.csanecki.memory.engine;

import java.util.Set;

public record MemoryGameSetup(Set<GroupToGuess> groupsToGuesses) {

    public record GroupToGuess(Set<FlatItemId> flatItemIds) {
    }
}
