package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.commons.DomainEvent;
import pl.cezarysanecki.memory.engine.api.FlatItemId;

import java.time.Instant;
import java.util.UUID;

public interface FlatItemEvent extends DomainEvent {

    FlatItemId flatItemId();

    record TurnedObverseUp(UUID eventId, Instant when, FlatItemId flatItemId) implements FlatItemEvent {
        public TurnedObverseUp(FlatItemId flatItemId) {
            this(UUID.randomUUID(), Instant.now(), flatItemId);
        }
    }

    record TurnedReverseUp(UUID eventId, Instant when, FlatItemId flatItemId) implements FlatItemEvent {
        public TurnedReverseUp(FlatItemId flatItemId) {
            this(UUID.randomUUID(), Instant.now(), flatItemId);
        }
    }

}
