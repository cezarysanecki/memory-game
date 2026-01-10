package pl.csanecki.memory.engine;

import java.util.UUID;

public record MemoryGameId(
        UUID sessionId
) {

    public static MemoryGameId init() {
        return new MemoryGameId(UUID.randomUUID());
    }

}
