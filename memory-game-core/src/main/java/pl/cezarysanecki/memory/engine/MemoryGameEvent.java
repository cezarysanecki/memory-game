package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.commons.DomainEvent;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public sealed interface MemoryGameEvent extends DomainEvent {

    MemoryGameId memoryGameId();

    List<FlatItemsGroupEvent> events();

    record Initialized(
            UUID eventId,
            Instant when,
            MemoryGameId memoryGameId,
            List<FlatItemsGroupEvent> events
    ) implements MemoryGameEvent {
        public Initialized(MemoryGameId memoryGameId, List<FlatItemsGroupEvent> events) {
            this(UUID.randomUUID(), Instant.now(), memoryGameId, events);
        }
    }

    record Continued(
            UUID eventId,
            Instant when,
            MemoryGameId memoryGameId,
            List<FlatItemsGroupEvent> events
    ) implements MemoryGameEvent {
        public Continued(MemoryGameId memoryGameId, List<FlatItemsGroupEvent> events) {
            this(UUID.randomUUID(), Instant.now(), memoryGameId, events);
        }
    }

    record Guessed(
            UUID eventId,
            Instant when,
            MemoryGameId memoryGameId,
            List<FlatItemsGroupEvent> events
    ) implements MemoryGameEvent {
        public Guessed(MemoryGameId memoryGameId, List<FlatItemsGroupEvent> events) {
            this(UUID.randomUUID(), Instant.now(), memoryGameId, events);
        }
    }

    record Missed(
            UUID eventId,
            Instant when,
            MemoryGameId memoryGameId,
            List<FlatItemsGroupEvent> events
    ) implements MemoryGameEvent {
        public Missed(MemoryGameId memoryGameId, List<FlatItemsGroupEvent> events) {
            this(UUID.randomUUID(), Instant.now(), memoryGameId, events);
        }
    }

    record Finished(
            UUID eventId,
            Instant when,
            MemoryGameId memoryGameId,
            List<FlatItemsGroupEvent> events
    ) implements MemoryGameEvent {
        public Finished(MemoryGameId memoryGameId, List<FlatItemsGroupEvent> events) {
            this(UUID.randomUUID(), Instant.now(), memoryGameId, events);
        }
    }

}
