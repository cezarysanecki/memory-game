package pl.csanecki.memory;

import javax.swing.*;
import java.awt.*;

public class GraphicCard extends JLabel {

    String filename;
    ImageIcon reverse;
    ImageIcon observe;
    ImageIcon activeIcon;
    boolean guessed = false;

    final FlatItem flatItem = FlatItem.reverseUp(FlatItemId.of(1));

    public GraphicCard(String name, String reverse, String observe) {
        setPreferredSize(new Dimension(100, 100));
        this.filename = name;
        this.reverse = new ImageIcon(getClass().getResource(reverse));
        this.observe = new ImageIcon(getClass().getResource(observe));
        this.activeIcon = this.reverse;
        setIcon(this.activeIcon);
    }

    public void turnCard() {
        this.activeIcon = resolveTurnedCard();
        setIcon(this.activeIcon);
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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.activeIcon.getImage(), 0, 0, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        GraphicCard graphicCard = (GraphicCard) obj;
        return this.filename.equals(graphicCard.filename);
    }
}
