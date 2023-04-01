package pl.csanecki.memory;

import javax.swing.*;
import java.awt.*;

public class GraphicCard extends JLabel {

    final FlatItem flatItem = new FlatItem(CardId.of(1));

    public GraphicCard(String name, String reverse, String observe) {
        setPreferredSize(new Dimension(100, 100));
        this.flatItem.filename = name;
        this.flatItem.reverse = new ImageIcon(reverse);
        this.flatItem.observe = new ImageIcon(observe);
        this.flatItem.activeIcon = this.flatItem.reverse;
        setIcon(this.flatItem.activeIcon);
    }

    public void turnCard() {
        this.flatItem.activeIcon = flatItem.resolveTurnedCard();
        setIcon(this.flatItem.activeIcon);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(flatItem.activeIcon.getImage(), 0, 0, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        GraphicCard graphicCard = (GraphicCard) obj;
        return this.flatItem.filename.equals(graphicCard.flatItem.filename);
    }
}
