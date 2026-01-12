package pl.cezarysanecki.memory.engine;

import java.util.UUID;

public record MemoryGameId(UUID value) {
    public static MemoryGameId create() {
        return new MemoryGameId(UUID.randomUUID());
    }
}
