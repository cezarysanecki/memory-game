package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.state.FlatItemCurrentState;

import javax.swing.*;
import java.awt.*;

public class GraphicCard extends JLabel {

    ImageIcon reverseIcon;
    ImageIcon averseIcon;
    boolean averse = false;

    final FlatItemId flatItemId;

    public GraphicCard(String reverseIcon, String averseIcon, FlatItemId flatItemId) {
        setPreferredSize(new Dimension(100, 100));
        this.reverseIcon = new ImageIcon(getClass().getResource(reverseIcon));
        this.averseIcon = new ImageIcon(getClass().getResource(averseIcon));
        this.flatItemId = flatItemId;
        setIcon(currentIcon());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(currentIcon().getImage(), 0, 0, null);
    }

    private ImageIcon currentIcon() {
        return averse ? averseIcon : reverseIcon;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        GraphicCard graphicCard = (GraphicCard) obj;
        return this.flatItemId.equals(graphicCard.flatItemId);
    }

    public FlatItemId getFlatItemId() {
        return flatItemId;
    }

    public void refresh(FlatItemCurrentState flatItem) {
        averse = flatItem.averse();
        setIcon(currentIcon());
    }
}
