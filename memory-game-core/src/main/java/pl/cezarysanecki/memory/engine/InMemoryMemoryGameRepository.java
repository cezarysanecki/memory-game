package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.db.MemoryGameEntity;
import pl.cezarysanecki.memory.engine.db.MemoryGameRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryMemoryGameRepository implements MemoryGameRepository {

    private static final Map<MemoryGameId, MemoryGameEntity> DATABASE = new ConcurrentHashMap<>();

    @Override
    public void save(MemoryGameEntity entity) {
        DATABASE.put(entity.memoryGameId(), entity);
    }

    @Override
    public MemoryGameEntity load(MemoryGameId memoryGameId) {
        return Optional.ofNullable(DATABASE.get(memoryGameId))
                .orElseThrow(() -> new IllegalArgumentException("Memory game not found: " + memoryGameId));
    }
}
