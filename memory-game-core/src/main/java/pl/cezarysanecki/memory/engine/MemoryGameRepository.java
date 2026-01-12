package pl.cezarysanecki.memory.engine;

public interface MemoryGameRepository {

    void save(MemoryGameState memoryGameState);
    MemoryGameState load(MemoryGameId memoryGameId);

}
