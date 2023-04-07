package pl.csanecki.memory.engine;

public class FlatItemsGroupId {

    private final int id;

    private FlatItemsGroupId(int id) {
        this.id = id;
    }

    public static FlatItemsGroupId of(int id) {
        return new FlatItemsGroupId(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
