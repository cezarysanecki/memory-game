package pl.csanecki.memory.setup;

import pl.csanecki.memory.ui.items.GraphicCard;
import pl.csanecki.memory.engine.FlatItemId;

import javax.swing.*;

public record GameCard(FlatItemId flatItemId, ImageIcon reserveIcon, ImageIcon obverseIcon) {

    public GraphicCard toGraphicCard() {
        return new GraphicCard(flatItemId, reserveIcon, obverseIcon);
    }
}
