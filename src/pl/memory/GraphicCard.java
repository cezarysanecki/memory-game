package pl.memory;

import javax.swing.*;
import java.awt.*;

public class GraphicCard extends JLabel {

    final CardLogic cardLogic = new CardLogic(1);

    public GraphicCard(String name, String reverse, String observe) {
        setPreferredSize(new Dimension(100, 100));
        this.cardLogic.filename = name;
        this.cardLogic.reverse = new ImageIcon(reverse);
        this.cardLogic.observe = new ImageIcon(observe);
        this.cardLogic.activeIcon = this.cardLogic.reverse;
        setIcon(this.cardLogic.activeIcon);
    }

    public void turnCard() {
        this.cardLogic.activeIcon = cardLogic.resolveTurnedCard();
        setIcon(this.cardLogic.activeIcon);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(cardLogic.activeIcon.getImage(), 0, 0, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        GraphicCard graphicCard = (GraphicCard) obj;
        return this.cardLogic.filename.equals(graphicCard.cardLogic.filename);
    }
}
