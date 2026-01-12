package pl.cezarysanecki.memory.engine.db;

import pl.cezarysanecki.memory.engine.api.MemoryGameId;

public interface MemoryGameRepository {

    void save(MemoryGameEntity entity);
    MemoryGameEntity load(MemoryGameId memoryGameId);

}
