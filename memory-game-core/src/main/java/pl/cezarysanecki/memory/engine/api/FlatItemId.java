package pl.cezarysanecki.memory.engine.api;

public record FlatItemId(int id) {

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
    public String toString() {
        return String.valueOf(id);
    }
}
