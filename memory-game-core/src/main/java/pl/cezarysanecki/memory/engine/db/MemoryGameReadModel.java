package pl.cezarysanecki.memory.engine.db;

import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;

import java.util.Optional;

public interface MemoryGameReadModel {

    MemoryGameState upsert(MemoryGameState memoryGameState);

    Optional<MemoryGameState> find(MemoryGameId memoryGameId);

    default MemoryGameState load(MemoryGameId memoryGameId) {
        return find(memoryGameId)
                .orElseThrow(() -> new IllegalStateException("Memory game with value " + memoryGameId + " not found"));
    }

}
