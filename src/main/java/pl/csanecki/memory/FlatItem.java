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

    public static FlatItem averse(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.AVERSE);
    }

    public static FlatItem reverse(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.REVERSE);
    }

    public void turnCard() {
        side = side == Side.AVERSE ? Side.REVERSE : Side.AVERSE;
    }

    public void turnToAverse() {
        side = Side.AVERSE;
    }

    public void turnToReverse() {
        side = Side.REVERSE;
    }

    public boolean isAverseSided() {
        return side == Side.AVERSE;
    }

    public boolean isReverseSided() {
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