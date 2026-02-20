package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;
import pl.cezarysanecki.memory.engine.db.MemoryGameReadModel;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryMemoryGameReadModel implements MemoryGameReadModel {

    private static final Map<MemoryGameId, MemoryGameState> DATABASE = new ConcurrentHashMap<>();

    @Override
    public MemoryGameState upsert(MemoryGameState memoryGameState) {
        return DATABASE.put(memoryGameState.memoryGameId(), memoryGameState);
    }

    @Override
    public Optional<MemoryGameState> find(MemoryGameId memoryGameId) {
        return Optional.ofNullable(DATABASE.get(memoryGameId));
    }
}
