package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class MemoryGameFactory {

    static MemoryGame create(int numberOfCards, int cardsInGroup) {
        if (numberOfCards % cardsInGroup != 0) {
            throw new IllegalArgumentException("number of cards must be dividable by cards in group");
        }
        if (numberOfCards <= 0 || cardsInGroup <= 0) {
            throw new IllegalArgumentException("arguments must be positive");
        }

        int numberOfGroups = numberOfCards / cardsInGroup;
        AtomicInteger flatItemGenerator = new AtomicInteger(0);

        List<FlatItemsGroup> flatItemsGroups = IntStream.range(0, numberOfGroups)
                .mapToObj(FlatItemsGroupId::of)
                .map(flatItemsGroupId -> FlatItemsGroup.allReversed(
                        flatItemsGroupId,
                        IntStream.range(0, cardsInGroup)
                                .mapToObj(index -> FlatItemId.of(flatItemGenerator.getAndIncrement()))
                                .collect(Collectors.toUnmodifiableSet())))
                .collect(Collectors.toList());

        Collections.shuffle(flatItemsGroups);

        return new MemoryGame(MemoryGameId.create(), new HashSet<>(flatItemsGroups));
    }

    static MemoryGame restore(MemoryGameState gameState) {
        Set<FlatItemsGroup> flatItemsGroups = gameState.flatItems().stream()
                .collect(Collectors.groupingBy(
                        MemoryGameState.FlatItem::assignedGroupId
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    Set<FlatItem> flatItems = entry.getValue().stream()
                            .map(flatItem -> FlatItem.restore(flatItem.flatItemId(), flatItem.obverseUp()))
                            .collect(Collectors.toSet());
                    return Map.entry(entry.getKey(), flatItems);
                })
                .map(entry -> new FlatItemsGroup(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());

        Set<FlatItemsGroup> guessed = flatItemsGroups.stream().filter(FlatItemsGroup::isAllObverseUp).collect(Collectors.toSet());

        FlatItemsGroup current = flatItemsGroups.stream().filter(group -> !group.isAllObverseUp() && !group.isAllReverseUp())
                .findFirst().orElse(null);

        return new MemoryGame(gameState.memoryGameId(), flatItemsGroups, guessed, current);
    }

}
