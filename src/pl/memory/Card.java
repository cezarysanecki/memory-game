package pl.memory;

import javax.swing.*;
import java.awt.*;

public class Card extends JLabel {

    private String name;
    private ImageIcon reverse;
    private ImageIcon observe;
    private ImageIcon activeIcon;
    private boolean guessed = false;

    public Card(String name, String reverse, String observe) {
        setPreferredSize(new Dimension(100, 100));
        this.name = name;
        this.reverse = new ImageIcon(reverse);
        this.observe = new ImageIcon(observe);
        this.activeIcon = this.reverse;
        setIcon(this.activeIcon);
    }

    public void setGuessed(boolean guessed) {
        this.guessed = guessed;
    }

    public boolean getGuessed() {
        return this.guessed;
    }

    public void turnCard() {
        if (this.activeIcon == this.reverse) {
            this.activeIcon = this.observe;
        } else {
            this.activeIcon = this.reverse;
        }
        setIcon(this.activeIcon);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(activeIcon.getImage(), 0, 0, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return this.name.equals(card.name);
    }
}
