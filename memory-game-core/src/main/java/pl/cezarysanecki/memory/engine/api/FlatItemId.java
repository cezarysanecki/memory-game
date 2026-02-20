package pl.cezarysanecki.memory.engine.api;

import java.util.Objects;
import java.util.UUID;

public record FlatItemId(UUID id) {

    public static FlatItemId create() {
        return new FlatItemId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatItemId that = (FlatItemId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
