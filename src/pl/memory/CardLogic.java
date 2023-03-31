package pl.memory;

import javax.swing.*;
import java.io.Serializable;

public class CardLogic implements Serializable {

    String name;
    ImageIcon reverse;
    ImageIcon observe;
    ImageIcon activeIcon;
    boolean guessed;
    private CardPosition cardPosition;

    public CardLogic() {
        cardPosition = CardPosition.REVERSE;
        guessed = false;
    }

    ImageIcon resolveTurnedCard() {
        if (this.activeIcon == this.reverse) {
            return this.observe;
        }
        return this.reverse;
    }

    CardPosition turnCard() {
        if (cardPosition == CardPosition.AVERSE) {
            cardPosition = CardPosition.REVERSE;
        } else {
            cardPosition = CardPosition.AVERSE;
        }
        return cardPosition;
    }

    void markAsGuessed() {
        this.guessed = true;
    }

    boolean isNotGuessed() {
        return !this.guessed;
    }
}