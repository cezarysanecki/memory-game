package pl.cezarysanecki.memory.engine;

import java.util.Objects;

public final class FlatItemsGroupId {

    private final int id;

    private FlatItemsGroupId(int id) {
        this.id = id;
    }

    public static FlatItemsGroupId of(int id) {
        return new FlatItemsGroupId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatItemsGroupId that = (FlatItemsGroupId) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
