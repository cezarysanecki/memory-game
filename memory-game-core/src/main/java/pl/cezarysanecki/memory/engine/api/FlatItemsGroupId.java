package pl.cezarysanecki.memory.engine.api;

import java.util.Objects;
import java.util.UUID;

public record FlatItemsGroupId(UUID value) {

    public static FlatItemsGroupId create() {
        return new FlatItemsGroupId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatItemsGroupId that = (FlatItemsGroupId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
