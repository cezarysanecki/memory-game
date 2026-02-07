package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.commons.DomainEvent;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public record MemoryGameEvent(
        UUID eventId,
        Instant when,
        MemoryGameId memoryGameId,
        Collection<FlatItemGroupEvent> events
) implements DomainEvent {

    public MemoryGameEvent(MemoryGameId memoryGameId, Collection<FlatItemGroupEvent> events) {
        this(UUID.randomUUID(), Instant.now(), memoryGameId, events);
    }

}
