package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.commons.DomainEvent;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public record FlatItemGroupEvent(
        UUID eventId,
        Instant when,
        FlatItemsGroupId flatItemsGroupId,
        Collection<FlatItemEvent> events
) implements DomainEvent {

    public FlatItemGroupEvent(FlatItemsGroupId flatItemsGroupId, Collection<FlatItemEvent> events) {
        this(UUID.randomUUID(), Instant.now(), flatItemsGroupId, events);
    }

}
