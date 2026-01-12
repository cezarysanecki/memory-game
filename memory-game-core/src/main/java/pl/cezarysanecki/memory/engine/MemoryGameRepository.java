package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;

public interface MemoryGameRepository {

    void save(MemoryGameState memoryGameState);
    MemoryGameState load(MemoryGameId memoryGameId);

}
