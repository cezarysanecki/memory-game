package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.commons.DomainEvent;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface FlatItemsGroupEvent extends DomainEvent {

    FlatItemsGroupId flatItemsGroupId();

    List<FlatItemEvent> events();

    record Initialized(
            UUID eventId,
            Instant when,
            FlatItemsGroupId flatItemsGroupId,
            List<FlatItemEvent> events
    ) implements FlatItemsGroupEvent {
        Initialized(FlatItemsGroupId flatItemsGroupId, List<FlatItemEvent> events) {
            this(UUID.randomUUID(), Instant.now(), flatItemsGroupId, events);
        }
    }

    record FlatItemTurned(
            UUID eventId,
            Instant when,
            FlatItemsGroupId flatItemsGroupId,
            List<FlatItemEvent> events
    ) implements FlatItemsGroupEvent {
        FlatItemTurned(FlatItemsGroupId flatItemsGroupId, List<FlatItemEvent> events) {
            this(UUID.randomUUID(), Instant.now(), flatItemsGroupId, events);
        }
    }

    record AllTurnedObverse(
            UUID eventId,
            Instant when,
            FlatItemsGroupId flatItemsGroupId,
            List<FlatItemEvent> events
    ) implements FlatItemsGroupEvent {
        AllTurnedObverse(FlatItemsGroupId flatItemsGroupId, List<FlatItemEvent> events) {
            this(UUID.randomUUID(), Instant.now(), flatItemsGroupId, events);
        }
    }

    record AllTurnedReverse(
            UUID eventId,
            Instant when,
            FlatItemsGroupId flatItemsGroupId,
            List<FlatItemEvent> events
    ) implements FlatItemsGroupEvent {
        AllTurnedReverse(FlatItemsGroupId flatItemsGroupId, List<FlatItemEvent> events) {
            this(UUID.randomUUID(), Instant.now(), flatItemsGroupId, events);
        }
    }
}
