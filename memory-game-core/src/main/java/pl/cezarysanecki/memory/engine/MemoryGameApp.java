package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;
import pl.cezarysanecki.memory.engine.db.MemoryGameEventStore;
import pl.cezarysanecki.memory.engine.db.MemoryGameReadModel;

import java.util.List;

public class MemoryGameApp {

    private final MemoryGameEventStore memoryGameEventStore;
    private final MemoryGameReadModel memoryGameReadModel;

    public MemoryGameApp(
            MemoryGameEventStore memoryGameEventStore,
            MemoryGameReadModel memoryGameReadModel
    ) {
        this.memoryGameEventStore = memoryGameEventStore;
        this.memoryGameReadModel = memoryGameReadModel;
    }

    public static MemoryGameApp inMemory() {
        InMemoryMemoryGameReadModel memoryGameReadModel = new InMemoryMemoryGameReadModel();
        InMemoryMemoryGameEventStore memoryGameEventStore = new InMemoryMemoryGameEventStore(memoryGameReadModel);
        return new MemoryGameApp(memoryGameEventStore, memoryGameReadModel);
    }

    public MemoryGameState start(int numberOfCards, int cardsInGroup) {
        MemoryGameEvent event = MemoryGame.createNewOne(numberOfCards, cardsInGroup);

        memoryGameEventStore.store(event);

        return memoryGameReadModel.load(event.memoryGameId());
    }

    public MemoryGameState turnCard(MemoryGameId memoryGameId, FlatItemId flatItemId) {
        List<MemoryGameEvent> events = memoryGameEventStore.load(memoryGameId);
        MemoryGame game = MemoryGame.restore(events);

        game.turnCard(flatItemId)
                .ifPresent(memoryGameEventStore::store);

        return memoryGameReadModel.load(memoryGameId);
    }

    public MemoryGameState getState(MemoryGameId memoryGameId) {
        return memoryGameReadModel.load(memoryGameId);
    }

}
