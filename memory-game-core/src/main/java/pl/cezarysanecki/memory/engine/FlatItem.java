package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;

import java.util.Objects;

final class FlatItem {

    private enum Side {
        Reverse, Obverse
    }

    private final FlatItemId flatItemId;
    private Side side;

    private FlatItem(FlatItemId flatItemId, Side side) {
        this.flatItemId = flatItemId;
        this.side = side;
    }

    static FlatItem obverseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.Obverse);
    }

    static FlatItem reverseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.Reverse);
    }

    void flip() {
        side = side == Side.Obverse ? Side.Reverse : Side.Obverse;
    }

    void turnObverseUp() {
        side = Side.Obverse;
    }

    void turnReverseUp() {
        side = Side.Reverse;
    }

    boolean isObverseUp() {
        return side == Side.Obverse;
    }

    boolean isReverseUp() {
        return side == Side.Reverse;
    }

    FlatItemId getFlatItemId() {
        return flatItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatItem flatItem = (FlatItem) o;
        return Objects.equals(flatItemId, flatItem.flatItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatItemId);
    }

}