package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.state.FlatItemCurrentState;

import javax.swing.*;
import java.awt.*;

public class GraphicCard extends JLabel {

    public static final int REQUIRED_WIDTH = 100;
    public static final int REQUIRED_HEIGHT = 100;

    ImageIcon reverseIcon;
    ImageIcon averseIcon;
    boolean averse = false;

    final FlatItemId flatItemId;

    public GraphicCard(ImageIcon reverseIcon, ImageIcon averseIcon, FlatItemId flatItemId) {
        setPreferredSize(new Dimension(REQUIRED_WIDTH, REQUIRED_HEIGHT));
        this.reverseIcon = reverseIcon;
        this.averseIcon = averseIcon;
        this.flatItemId = flatItemId;

        setIcon(currentIcon());
    }

    public void refresh(FlatItemCurrentState flatItem) {
        averse = flatItem.averse();
        setIcon(currentIcon());
    }

    public FlatItemId getFlatItemId() {
        return flatItemId;
    }

    private ImageIcon currentIcon() {
        return averse ? averseIcon : reverseIcon;
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
        return this.flatItemId.equals(graphicCard.flatItemId);
    }
}
