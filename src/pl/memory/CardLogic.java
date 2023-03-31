package pl.memory;

import javax.swing.*;
import java.io.Serializable;

public class CardLogic implements Serializable {

    String filename;
    ImageIcon reverse;
    ImageIcon observe;
    ImageIcon activeIcon;

    int id;
    boolean guessed;
    private CardPosition cardPosition;

    public CardLogic(int id) {
        this.id = id;
        this.cardPosition = CardPosition.REVERSE;
        this.guessed = false;
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