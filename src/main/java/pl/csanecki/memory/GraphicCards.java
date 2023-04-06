package pl.csanecki.memory;

import pl.csanecki.memory.state.GroupOfFlatItemsCurrentState;
import pl.csanecki.memory.state.MemoryGameCurrentState;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        return List.copyOf(graphicCards);
    }
}