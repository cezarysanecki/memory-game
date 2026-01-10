package pl.csanecki.memory.engine.game;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroup;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.engine.MemoryGameState;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemoryGameFactory {

    public static MemoryGame create(int numberOfCards, int cardsInGroup) {
        if (numberOfCards <= 0 || cardsInGroup <= 0) {
            throw new IllegalArgumentException("arguments must be positive");
        }
        if (numberOfCards % cardsInGroup != 0) {
            throw new IllegalArgumentException("number of cards must be dividable by cards in group");
        }

        int numberOfGroups = numberOfCards / cardsInGroup;
        AtomicInteger flatItemIdSequenceGenerator = new AtomicInteger(0);

        Set<FlatItemsGroup> flatItemsGroups = IntStream.range(0, numberOfGroups)
                .mapToObj(FlatItemsGroupId::of)
                .map(flatItemsGroupId -> FlatItemsGroup.allReversed(
                        flatItemsGroupId,
                        IntStream.range(0, cardsInGroup)
                                .mapToObj(index -> FlatItemId.of(flatItemIdSequenceGenerator.getAndIncrement()))
                                .collect(Collectors.toUnmodifiableSet())))
                .collect(Collectors.toUnmodifiableSet());
        return new MemoryGame(flatItemsGroups);
    }

    public static MemoryGame restore(MemoryGameState memoryGameState) {

    }

}
