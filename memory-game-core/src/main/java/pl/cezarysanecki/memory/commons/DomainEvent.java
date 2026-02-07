package pl.cezarysanecki.memory.commons;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

    UUID eventId();

    Instant when();

}
