package pl.csanecki.memory;

import javax.swing.*;
import java.io.Serializable;

public class FlatItem implements Serializable {

    enum Side {
        REVERSE, AVERSE
    }

    String filename;
    ImageIcon reverse;
    ImageIcon observe;
    ImageIcon activeIcon;
    boolean guessed;

    private FlatItemId flatItemId;
    private Side side;

    public FlatItem(FlatItemId flatItemId) {
        this.flatItemId = flatItemId;
        this.side = Side.REVERSE;
        this.guessed = false;
    }

    ImageIcon resolveTurnedCard() {
        if (this.activeIcon == this.reverse) {
            return this.observe;
        }
        return this.reverse;
    }

    void turnCard() {
        side = side == Side.AVERSE ? Side.REVERSE : Side.AVERSE;
    }

    boolean isAverseSided() {
        return side == Side.AVERSE;
    }

    boolean isReverseSided() {
        return side == Side.REVERSE;
    }

    void markAsGuessed() {
        this.guessed = true;
    }

    boolean isNotGuessed() {
        return !this.guessed;
    }
}