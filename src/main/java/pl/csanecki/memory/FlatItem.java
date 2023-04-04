package pl.csanecki.memory;

import java.io.Serializable;

public class FlatItem implements Serializable {

    private enum Side {
        REVERSE, AVERSE
    }

    private final FlatItemId flatItemId;
    private Side side;

    private FlatItem(FlatItemId flatItemId, Side side) {
        this.flatItemId = flatItemId;
        this.side = side;
    }

    public static FlatItem averseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.AVERSE);
    }

    public static FlatItem reverseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.REVERSE);
    }

    public void flip() {
        side = side == Side.AVERSE ? Side.REVERSE : Side.AVERSE;
    }

    public void turnAverseUp() {
        side = Side.AVERSE;
    }

    public void turnReverseUp() {
        side = Side.REVERSE;
    }

    public boolean isAverseUp() {
        return side == Side.AVERSE;
    }

    public boolean isReverseUp() {
        return side == Side.REVERSE;
    }

    public FlatItemId getFlatItemId() {
        return flatItemId;
    }
}