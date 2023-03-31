package pl.memory;

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

    private CardId cardId;
    private Side side;

    public FlatItem(CardId cardId) {
        this.cardId = cardId;
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

    boolean isAverseSide() {
        return side == Side.AVERSE;
    }

    void markAsGuessed() {
        this.guessed = true;
    }

    boolean isNotGuessed() {
        return !this.guessed;
    }
}