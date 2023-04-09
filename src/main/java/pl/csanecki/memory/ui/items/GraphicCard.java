package pl.csanecki.memory.ui.items;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.state.FlatItemCurrentState;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class GraphicCard extends JLabel {

    private final FlatItemId flatItemId;
    private final ImageIcon reverseIcon;
    private final ImageIcon obverseIcon;

    public GraphicCard(FlatItemId flatItemId, ImageIcon reverseIcon, ImageIcon obverseIcon, boolean obverse) {
        setPreferredSize(new Dimension(reverseIcon.getIconWidth(), reverseIcon.getIconHeight()));

        this.flatItemId = flatItemId;
        this.reverseIcon = reverseIcon;
        this.obverseIcon = obverseIcon;

        setIcon(currentIcon(obverse));
    }

    public void refresh(FlatItemCurrentState flatItem) {
        setIcon(currentIcon(flatItem.obverse()));
    }

    public FlatItemId getFlatItemId() {
        return flatItemId;
    }

    private ImageIcon currentIcon(boolean obverse) {
        return obverse ? obverseIcon : reverseIcon;
    }

    public boolean contains(Point2D point) {
        Rectangle rectangle = new Rectangle(
            this.getLocation().x, this.getLocation().y,
            this.getWidth(), this.getHeight());
        return rectangle.contains(point);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        ImageIcon currentImageIcon = (ImageIcon) getIcon();
        g2.drawImage(currentImageIcon.getImage(), 0, 0, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        GraphicCard graphicCard = (GraphicCard) obj;
        return this.flatItemId.equals(graphicCard.flatItemId);
    }

    public void turnToObverseUp() {
        setIcon(currentIcon(true));
    }
}
