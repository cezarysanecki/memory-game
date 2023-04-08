package pl.csanecki.memory.ui.items;

import pl.csanecki.memory.engine.state.GroupOfFlatItemsCurrentState;
import pl.csanecki.memory.engine.state.MemoryGameCurrentState;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GraphicCards {

    private final List<GraphicCard> graphicCards;

    public GraphicCards(List<GraphicCard> graphicCards) {
        this.graphicCards = graphicCards;
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