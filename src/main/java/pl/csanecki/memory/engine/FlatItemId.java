package pl.csanecki.memory.engine;

public class FlatItemId {

    private final int id;

    private FlatItemId(int id) {
        this.id = id;
    }

    public static FlatItemId of(int id) {
        return new FlatItemId(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
