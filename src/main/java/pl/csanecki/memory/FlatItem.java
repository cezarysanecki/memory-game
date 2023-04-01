package pl.csanecki.memory;

import javax.swing.*;
import java.io.Serializable;

public class FlatItem implements Serializable {

    private enum Side {
        REVERSE, AVERSE
    }

    String filename;
    ImageIcon reverse;
    ImageIcon observe;
    ImageIcon activeIcon;
    boolean guessed;

    private final FlatItemId flatItemId;
    private Side side;

    private FlatItem(FlatItemId flatItemId, Side side) {
        this.flatItemId = flatItemId;
        this.side = side;
        this.guessed = false;
    }

    public static FlatItem averseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.AVERSE);
    }

    public static FlatItem reverseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.REVERSE);
    }

    public void turnAround() {
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

    ImageIcon resolveTurnedCard() {
        if (this.activeIcon == this.reverse) {
            return this.observe;
        }
        return this.reverse;
    }

    void markAsGuessed() {
        this.guessed = true;
    }

    boolean isNotGuessed() {
        return !this.guessed;
    }
}