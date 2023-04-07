package pl.csanecki.memory.setup;

import pl.csanecki.memory.GraphicCard;
import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroupId;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupOfGameCards {

    private final FlatItemsGroupId flatItemsGroupId;
    private final Set<GameCard> gameCards;

    public GroupOfGameCards(FlatItemsGroupId flatItemsGroupId, Set<GameCard> gameCards) {
        this.flatItemsGroupId = flatItemsGroupId;
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

    public FlatItemsGroupId getFlatItemsGroupId() {
        return flatItemsGroupId;
    }
}
