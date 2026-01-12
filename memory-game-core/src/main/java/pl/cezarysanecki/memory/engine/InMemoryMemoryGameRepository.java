package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;
import pl.cezarysanecki.memory.engine.db.MemoryGameRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryMemoryGameRepository implements MemoryGameRepository {

    private static final Map<MemoryGameId, MemoryGameState> DATABASE = new ConcurrentHashMap<>();

    @Override
    public void save(MemoryGameState entity) {
        DATABASE.put(entity.memoryGameId(), entity);
    }

    @Override
    public MemoryGameState load(MemoryGameId memoryGameId) {
        return Optional.ofNullable(DATABASE.get(memoryGameId))
                .orElseThrow(() -> new IllegalArgumentException("Memory game not found: " + memoryGameId));
    }
}
