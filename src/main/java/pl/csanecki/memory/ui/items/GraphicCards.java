package pl.csanecki.memory.ui.items;

import pl.csanecki.memory.engine.state.FlatItemCurrentState;
import pl.csanecki.memory.engine.state.GroupOfFlatItemsCurrentState;
import pl.csanecki.memory.engine.state.MemoryGameCurrentState;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GraphicCards {

    private final List<GraphicCard> graphicCards;

    private GraphicCards(List<GraphicCard> graphicCards) {
        this.graphicCards = graphicCards;
    }

    public static GraphicCards createShuffled(MemoryGameCurrentState currentState, ImageIcon reverseImage, List<ImageIcon> obverseImages) {
        List<GraphicCard> graphicCards = new ArrayList<>();
        int index = 0;
        for (GroupOfFlatItemsCurrentState groupOfFlatItemsCurrentState : currentState.groupOfFlatItems()) {
            ImageIcon obverseImage = obverseImages.get(index);
            for (FlatItemCurrentState flatItemCurrentState : groupOfFlatItemsCurrentState.flatItems()) {
                graphicCards.add(new GraphicCard(
                    flatItemCurrentState.flatItemId(),
                    reverseImage,
                    obverseImage,
                    flatItemCurrentState.obverse()));
            }
            index++;
        }
        Collections.shuffle(graphicCards);
        return new GraphicCards(graphicCards);
    }

    public Optional<GraphicCard> findCardByCoordinates(Point2D point) {
        return graphicCards.stream()
            .filter(graphicCard -> graphicCard.contains(point))
            .findFirst();
    }

    public void refreshAll(MemoryGameCurrentState currentState) {
        graphicCards.forEach(graphicCard -> currentState.groupOfFlatItems()
            .stream()
            .map(GroupOfFlatItemsCurrentState::flatItems)
            .flatMap(Collection::stream)
            .filter(flatItem -> flatItem.flatItemId().equals(graphicCard.getFlatItemId()))
            .findFirst()
            .ifPresent(graphicCard::refresh));
    }
}