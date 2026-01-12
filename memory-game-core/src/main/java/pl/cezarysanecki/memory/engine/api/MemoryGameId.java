package pl.cezarysanecki.memory.engine.api;

import java.util.UUID;

public record MemoryGameId(UUID value) {
    public static MemoryGameId create() {
        return new MemoryGameId(UUID.randomUUID());
    }
}
