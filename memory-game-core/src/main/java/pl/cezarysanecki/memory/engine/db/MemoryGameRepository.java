package pl.cezarysanecki.memory.engine.db;

import pl.cezarysanecki.memory.engine.api.MemoryGameState;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;

public interface MemoryGameRepository {

    void save(MemoryGameState state);

    MemoryGameState load(MemoryGameId memoryGameId);

}
