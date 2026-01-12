package pl.cezarysanecki.memory.engine.state;

import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.MemoryGameRepository;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMemoryGameRepository implements MemoryGameRepository {

    private static final Map<MemoryGameId, MemoryGameState> DATABASE = new ConcurrentHashMap<>();

    @Override
    public void save(MemoryGameState memoryGame) {
        DATABASE.put(memoryGame.memoryGameId(), memoryGame);
    }

    @Override
    public MemoryGameState load(MemoryGameId memoryGameId) {
        return Optional.ofNullable(DATABASE.get(memoryGameId))
                .orElseThrow(() -> new IllegalArgumentException("Memory game not found: " + memoryGameId));
    }
}
