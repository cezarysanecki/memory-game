package pl.cezarysanecki.memory.engine;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemoryGameFactory {

    public static MemoryGame create(int numberOfCards, int cardsInGroup) {
        if (numberOfCards % cardsInGroup != 0) {
            throw new IllegalArgumentException("number of cards must be dividable by cards in group");
        }
        if (numberOfCards <= 0 || cardsInGroup <= 0) {
            throw new IllegalArgumentException("arguments must be positive");
        }

        int numberOfGroups = numberOfCards / cardsInGroup;
        AtomicInteger flatItemGenerator = new AtomicInteger(0);

        Set<FlatItemsGroup> flatItemsGroups = IntStream.range(0, numberOfGroups)
                .mapToObj(FlatItemsGroupId::of)
                .map(flatItemsGroupId -> FlatItemsGroup.allReversed(
                        flatItemsGroupId,
                        IntStream.range(0, cardsInGroup)
                                .mapToObj(_ -> FlatItemId.of(flatItemGenerator.getAndIncrement()))
                                .collect(Collectors.toUnmodifiableSet())))
                .collect(Collectors.toUnmodifiableSet());

        return new MemoryGame(flatItemsGroups, new HashSet<>(), null);
    }

    public static MemoryGame restore(MemoryGameState memoryGameState) {
        Set<FlatItemsGroup> flatItemsGroups = memoryGameState.flatItems().stream()
                .collect(Collectors.groupingBy(
                        MemoryGameState.FlatItem::assignedGroupId
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    Set<FlatItem> flatItems = entry.getValue().stream()
                            .map(flatItem -> {
                                if (flatItem.obverseUp()) {
                                    return FlatItem.obverseUp(flatItem.flatItemId());
                                } else {
                                    return FlatItem.reverseUp(flatItem.flatItemId());
                                }
                            })
                            .collect(Collectors.toSet());
                    return Map.entry(entry.getKey(), flatItems);
                })
                .map(entry -> new FlatItemsGroup(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());

        Set<FlatItemsGroup> guessed = flatItemsGroups.stream().filter(FlatItemsGroup::isAllObverseUp).collect(Collectors.toSet());

        FlatItemsGroup current = flatItemsGroups.stream().filter(group -> !group.isAllObverseUp() && !group.isAllReverseUp())
                .findFirst().orElse(null);

        return new MemoryGame(flatItemsGroups, guessed, current);
    }

}
