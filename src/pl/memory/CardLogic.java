package pl.memory;

import javax.swing.*;
import java.io.Serializable;

public class CardLogic implements Serializable {
    String name;
    ImageIcon reverse;
    ImageIcon observe;
    ImageIcon activeIcon;
    boolean guessed = false;

    public CardLogic() {
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