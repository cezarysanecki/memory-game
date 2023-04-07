package pl.csanecki.memory;

import pl.csanecki.memory.engine.state.GroupOfFlatItemsCurrentState;
import pl.csanecki.memory.engine.state.MemoryGameCurrentState;

import java.awt.geom.Point2D;
import java.util.*;

public class GraphicCards {

    private final Set<GraphicCard> graphicCards;

    public GraphicCards(Set<GraphicCard> graphicCards) {
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

    public List<GraphicCard> getGraphicCards() {
        return new ArrayList<>(graphicCards);
    }
}