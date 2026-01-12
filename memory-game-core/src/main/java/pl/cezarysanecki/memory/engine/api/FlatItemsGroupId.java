package pl.cezarysanecki.memory.engine.api;

public record FlatItemsGroupId(int id) {

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
    public String toString() {
        return String.valueOf(id);
    }
}
