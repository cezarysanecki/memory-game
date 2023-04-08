package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.engine.MemoryGameSetup;
import pl.csanecki.memory.ui.items.GraphicCard;
import pl.csanecki.memory.ui.items.GraphicCards;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Divider {

    private final Map<GroupOfGraphicCards, Set<FlatItemId>> map;

    private Divider(Map<GroupOfGraphicCards, Set<FlatItemId>> map) {
        this.map = map;
    }

    public static Divider create(EngineGameConfig gameConfig) {
        int numberOfCards = gameConfig.columns * gameConfig.rows;

        Set<FlatItemId> flatItemIds = IntStream.range(0, numberOfCards)
            .mapToObj(FlatItemId::of)
            .collect(Collectors.toUnmodifiableSet());

        int divide = numberOfCards / gameConfig.numberOfCardsInGroup;
        int rest = numberOfCards % gameConfig.numberOfCardsInGroup;
        int numberOfGroups = divide + ((rest == 0) ? 0 : 1);

        Set<GroupOfGraphicCards> groupOfGraphicCardsSet = IntStream.range(0, numberOfGroups)
            .mapToObj(index -> new GroupOfGraphicCards(
                FlatItemsGroupId.of(index),
                gameConfig.reverseImage,
                gameConfig.obverseImages.get(index)))
            .collect(Collectors.toUnmodifiableSet());

        Map<GroupOfGraphicCards, Set<FlatItemId>> foo = new HashMap<>();

        int counter = 0;
        for (FlatItemId flatItemId : flatItemIds) {
            for (GroupOfGraphicCards groupOfGraphicCards : groupOfGraphicCardsSet) {
                Set<FlatItemId> added = foo.getOrDefault(groupOfGraphicCards, new HashSet<>());
                added.add(flatItemId);
                foo.put(groupOfGraphicCards, added);
                if (++counter >= gameConfig.numberOfCardsInGroup) {
                    break;
                }
            }
        }
        return new Divider(foo);
    }

    private record GroupOfGraphicCards(FlatItemsGroupId flatItemsGroupId,
                                       ImageIcon reverseImage,
                                       ImageIcon obverseImage) {
    }

    public MemoryGameSetup toMemoryGameSetup() {
        Set<MemoryGameSetup.GroupToGuess> groupsToGuesses = map.entrySet()
            .stream()
            .map(entry -> new MemoryGameSetup.GroupToGuess(entry.getKey().flatItemsGroupId(), entry.getValue()))
            .collect(Collectors.toUnmodifiableSet());
        return new MemoryGameSetup(groupsToGuesses);
    }

    public GraphicCards toGraphicCards() {
        Set<GraphicCard> graphicCards = map.entrySet()
            .stream()
            .flatMap(entry -> entry.getValue()
                .stream()
                .map(flatItemId -> new GraphicCard(
                    flatItemId,
                    entry.getKey().reverseImage(),
                    entry.getKey().obverseImage())))
            .collect(Collectors.toUnmodifiableSet());
        return new GraphicCards(graphicCards);
    }
}
