package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItem;
import pl.csanecki.memory.engine.FlatItemId;

import javax.swing.*;
import java.awt.*;

public class GraphicCard extends JLabel {

    String filename;
    ImageIcon reverseIcon;
    ImageIcon averseIcon;
    boolean guessed = false;

    final FlatItem flatItem;

    public GraphicCard(String name, String reverseIcon, String averseIcon) {
        setPreferredSize(new Dimension(100, 100));
        this.filename = name;
        this.reverseIcon = new ImageIcon(getClass().getResource(reverseIcon));
        this.averseIcon = new ImageIcon(getClass().getResource(averseIcon));
        this.flatItem = FlatItem.reverseUp(FlatItemId.of(1));
        setIcon(currentIcon());
    }

    public void turnCard() {
        flatItem.flip();
        setIcon(currentIcon());
    }

    void markAsGuessed() {
        this.guessed = true;
    }

    boolean isNotGuessed() {
        return !this.guessed;
    }

    private ImageIcon currentIcon() {
        return flatItem.isAverseUp() ? this.averseIcon : this.reverseIcon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(currentIcon().getImage(), 0, 0, null);
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
