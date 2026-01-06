package pl.csanecki.memory.engine;

import java.util.Objects;

public final class FlatItemId {

    private final int id;

    private FlatItemId(int id) {
        this.id = id;
    }

    public static FlatItemId of(int id) {
        return new FlatItemId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatItemId that = (FlatItemId) o;
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
