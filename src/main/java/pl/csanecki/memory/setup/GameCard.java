package pl.csanecki.memory.setup;

import pl.csanecki.memory.GraphicCard;
import pl.csanecki.memory.engine.FlatItemId;

import javax.swing.*;

public record GameCard(FlatItemId flatItemId, ImageIcon reserveIcon, ImageIcon averseIcon) {

    public GraphicCard toGraphicCard() {
        return new GraphicCard(flatItemId, reserveIcon, averseIcon);
    }
}
