package pl.cezarysanecki.memory.engine;

import java.util.Objects;

public final class FlatItem {

    private enum Side {
        Reverse, Obverse
    }

    private final FlatItemId flatItemId;
    private Side side;

    private FlatItem(FlatItemId flatItemId, Side side) {
        this.flatItemId = flatItemId;
        this.side = side;
    }

    public static FlatItem obverseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.Obverse);
    }

    public static FlatItem reverseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.Reverse);
    }

    public void flip() {
        side = side == Side.Obverse ? Side.Reverse : Side.Obverse;
    }

    public void turnObverseUp() {
        side = Side.Obverse;
    }

    public void turnReverseUp() {
        side = Side.Reverse;
    }

    public boolean isObverseUp() {
        return side == Side.Obverse;
    }

    public boolean isReverseUp() {
        return side == Side.Reverse;
    }

    public FlatItemId getFlatItemId() {
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