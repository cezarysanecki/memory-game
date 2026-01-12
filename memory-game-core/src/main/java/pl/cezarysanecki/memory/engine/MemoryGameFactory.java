package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.db.MemoryGameEntity;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class MemoryGameFactory {

    static MemoryGameEntity create(int numberOfCards, int cardsInGroup) {
        if (numberOfCards % cardsInGroup != 0) {
            throw new IllegalArgumentException("number of cards must be dividable by cards in group");
        }
        if (numberOfCards <= 0 || cardsInGroup <= 0) {
            throw new IllegalArgumentException("arguments must be positive");
        }

        Set<MemoryGameEntity.FlatItem> flatItemsGroups = IntStream.range(0, numberOfCards)
                .mapToObj(flatItemId -> new MemoryGameEntity.FlatItem(
                                FlatItemId.of(flatItemId),
                                FlatItemsGroupId.of(flatItemId % (numberOfCards / cardsInGroup)),
                                false
                        )
                ).collect(Collectors.toUnmodifiableSet());

        return new MemoryGameEntity(MemoryGameId.create(), flatItemsGroups);
    }

    static MemoryGame restore(MemoryGameEntity memoryGameEntity) {
        Set<FlatItemsGroup> flatItemsGroups = memoryGameEntity.flatItems().stream()
                .collect(Collectors.groupingBy(
                        MemoryGameEntity.FlatItem::assignedGroupId
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
