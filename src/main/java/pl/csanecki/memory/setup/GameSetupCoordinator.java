package pl.csanecki.memory.setup;

import pl.csanecki.memory.ui.items.GraphicCard;
import pl.csanecki.memory.ui.items.GraphicCards;
import pl.csanecki.memory.engine.MemoryGameSetup;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record GameSetupCoordinator(Set<GroupOfGameCards> groupsOfGameCards) {

    public MemoryGameSetup toGameSetup() {
        Set<MemoryGameSetup.GroupToGuess> groupsToGuess = groupsOfGameCards.stream()
                .map(groupsOfGameCards -> new MemoryGameSetup.GroupToGuess(
                        groupsOfGameCards.getFlatItemsGroupId(),
                        groupsOfGameCards.getFlatItemIds()
                ))
                .collect(Collectors.toUnmodifiableSet());
        return new MemoryGameSetup(groupsToGuess);
    }

    public GraphicCards toGraphicCards() {
        Set<GraphicCard> graphicCards = groupsOfGameCards.stream()
                .map(GroupOfGameCards::getGraphicCards)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());
        return new GraphicCards(graphicCards);
    }

}

