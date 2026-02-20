package pl.cezarysanecki.memory.engine.db;

import pl.cezarysanecki.memory.engine.MemoryGameEvent;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;

import java.util.List;

public interface MemoryGameEventStore {

    void store(MemoryGameEvent state);

    List<MemoryGameEvent> load(MemoryGameId memoryGameId);

}
