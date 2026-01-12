package pl.cezarysanecki.memory.engine.db;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;
import pl.cezarysanecki.memory.engine.api.GuessResult;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public record MemoryGameEntity(
        MemoryGameId memoryGameId,
        Set<FlatItem> flatItems
) {
    public MemoryGameEntity applyResult(FlatItemId flatItemId, GuessResult guessResult) {
        switch (guessResult) {
            case Continue, Guessed, GameOver -> {
                Set<FlatItem> forwardedFlatItems = flatItems.stream()
                        .map(item -> {
                            if (item.flatItemId().equals(flatItemId)) {
                                return new FlatItem(
                                        item.flatItemId(),
                                        item.assignedGroupId(),
                                        true
                                );
                            } else {
                                return item;
                            }
                        })
                        .collect(Collectors.toUnmodifiableSet());
                return new MemoryGameEntity(memoryGameId, forwardedFlatItems);
            }
            case Failure -> {
                Map<FlatItemsGroupId, List<FlatItem>> grouped = flatItems.stream()
                        .collect(Collectors.groupingBy(FlatItem::assignedGroupId));

                Set<FlatItem> forwardedFlatItems = grouped.values().stream()
                        .map(items -> {
                            if (items.stream().allMatch(FlatItem::obverseUp) || items.stream().allMatch(not(FlatItem::obverseUp))) {
                                return items;
                            }
                            return items.stream().map(item -> new FlatItem(
                                            item.flatItemId, item.assignedGroupId, false
                                    ))
                                    .toList();
                        })
                        .flatMap(Collection::stream)
                        .collect(Collectors.toUnmodifiableSet());

                return new MemoryGameEntity(memoryGameId, forwardedFlatItems);
            }
        }

        return null;
    }

    public record FlatItem(
            FlatItemId flatItemId,
            FlatItemsGroupId assignedGroupId,
            boolean obverseUp
    ) {
    }
}
