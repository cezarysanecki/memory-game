package pl.cezarysanecki.memory.infrastructure;

import pl.cezarysanecki.memory.engine.MemoryGameApp;

public class MemoryGameAppFactory {

    public static MemoryGameApp inMemory() {
        InMemoryMemoryGameReadModel memoryGameReadModel = new InMemoryMemoryGameReadModel();
        InMemoryMemoryGameEventStore memoryGameEventStore = new InMemoryMemoryGameEventStore(memoryGameReadModel);
        return new MemoryGameApp(memoryGameEventStore, memoryGameReadModel);
    }

}
