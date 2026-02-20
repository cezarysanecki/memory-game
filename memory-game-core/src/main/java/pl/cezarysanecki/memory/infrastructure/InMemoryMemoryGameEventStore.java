package pl.cezarysanecki.memory.infrastructure;

import pl.cezarysanecki.memory.engine.FlatItemEvent;
import pl.cezarysanecki.memory.engine.MemoryGameEvent;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;
import pl.cezarysanecki.memory.engine.db.MemoryGameEventStore;
import pl.cezarysanecki.memory.engine.db.MemoryGameReadModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Comparator.comparing;

public class InMemoryMemoryGameEventStore implements MemoryGameEventStore {

    private final MemoryGameReadModel memoryGameReadModel;

    private static final Map<MemoryGameId, List<MemoryGameEvent>> DATABASE = new ConcurrentHashMap<>();

    public InMemoryMemoryGameEventStore(MemoryGameReadModel memoryGameReadModel) {
        this.memoryGameReadModel = memoryGameReadModel;
    }

    @Override
    public void store(MemoryGameEvent event) {
        List<MemoryGameEvent> events = DATABASE.getOrDefault(
                event.memoryGameId(),
                new ArrayList<>()
        );

        events.add(event);

        DATABASE.put(event.memoryGameId(), events);

        MemoryGameState memoryGameState;
        if (event instanceof MemoryGameEvent.Initialized initialized) {
            memoryGameState = MemoryGameState.initialize(initialized);
        } else {
            memoryGameState = memoryGameReadModel.load(event.memoryGameId());
        }

        MemoryGameState newState = event.events().stream()
                .flatMap(groupEvent -> groupEvent.events().stream())
                .sorted(comparing(FlatItemEvent::when))
                .reduce(memoryGameState, MemoryGameState::evolve, (left, right) -> right);

        if (event instanceof MemoryGameEvent.Finished) {
            newState = newState.markAsFinished();
        }

        memoryGameReadModel.upsert(newState);
    }

    @Override
    public List<MemoryGameEvent> load(MemoryGameId memoryGameId) {
        return Collections.unmodifiableList(DATABASE.getOrDefault(memoryGameId, List.of()));
    }
}
