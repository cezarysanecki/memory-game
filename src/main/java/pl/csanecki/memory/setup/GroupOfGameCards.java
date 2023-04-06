package pl.csanecki.memory.setup;

import pl.csanecki.memory.GraphicCard;
import pl.csanecki.memory.engine.FlatItemId;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupOfGameCards {

    private final Set<GameCard> gameCards;

    public GroupOfGameCards(Set<GameCard> gameCards) {
        this.gameCards = gameCards;
    }

    public Set<GraphicCard> getGraphicCards() {
        return gameCards.stream()
                .map(GameCard::toGraphicCard)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<FlatItemId> getFlatItemIds() {
        return gameCards.stream()
                .map(GameCard::flatItemId)
                .collect(Collectors.toUnmodifiableSet());
    }

}
