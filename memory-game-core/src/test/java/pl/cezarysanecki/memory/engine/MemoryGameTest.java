package pl.cezarysanecki.memory.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemoryGameTest {

    public static final MemoryGameId memoryGameId = MemoryGameId.create();

    @ParameterizedTest
    @CsvSource({"7,2", "2,7"})
    void cannot_create_game_for_not_dividable_number_of_cards_by_cards_in_group(
            int numberOfCards, int cardsInGroup
    ) {
        assertThrows(IllegalArgumentException.class,
                () -> MemoryGame.createNewOne(numberOfCards, cardsInGroup));
    }

    @ParameterizedTest
    @CsvSource({"-16,8", "4,-2", "0,2"})
    void cannot_create_game_for_not_positive_values(
            int numberOfCards, int cardsInGroup
    ) {
        assertThrows(IllegalArgumentException.class,
                () -> MemoryGame.createNewOne(numberOfCards, cardsInGroup));
    }

    @Test
    void turning_first_card_from_group_informs_to_continue_guessing() {
        MemoryGameEvent event = MemoryGame.createNewOne(4, 2);
        MemoryGame memoryGame = MemoryGame.restore(List.of(event));

        List<FlatItemsGroupId> flatItemsGroupIds = extractFlatItemGroupIds(List.of(event));
        List<FlatItemId> flatItemIds = extractFlatItemIdsFor(flatItemsGroupIds.getFirst(), List.of(event));

        Optional<MemoryGameEvent> result = memoryGame.turnCard(flatItemIds.getFirst());

        assertInstanceOf(MemoryGameEvent.Continued.class, result.get());
    }

    @Test
    void guessing_all_cards_in_group_requires_turning_them_all_in_row() {
        FlatItemId guessedFlatItemId = FlatItemId.create();

        FlatItemsGroup currentFlatItemsGroup = new FlatItemsGroup(FlatItemsGroupId.create(), Set.of(
                new FlatItem(FlatItemId.create(), FlatItem.Side.Obverse),
                new FlatItem(guessedFlatItemId, FlatItem.Side.Reverse)
        ));
        FlatItemsGroup secondFlatItemsGroup = new FlatItemsGroup(FlatItemsGroupId.create(), Set.of(
                new FlatItem(FlatItemId.create(), FlatItem.Side.Reverse),
                new FlatItem(FlatItemId.create(), FlatItem.Side.Reverse)
        ));

        MemoryGame memoryGame = new MemoryGame(
                memoryGameId,
                Set.of(
                        currentFlatItemsGroup,
                        secondFlatItemsGroup
                ),
                Set.of(),
                currentFlatItemsGroup
        );

        Optional<MemoryGameEvent> result = memoryGame.turnCard(guessedFlatItemId);

        assertInstanceOf(MemoryGameEvent.Guessed.class, result.get());
    }

    @Test
    void not_guessing_all_cards_in_group_in_row_determines_failure() {
        FlatItemId missedFlatItemId = FlatItemId.create();

        FlatItemsGroup currentFlatItemsGroup = new FlatItemsGroup(FlatItemsGroupId.create(), Set.of(
                new FlatItem(FlatItemId.create(), FlatItem.Side.Obverse),
                new FlatItem(FlatItemId.create(), FlatItem.Side.Reverse)
        ));
        FlatItemsGroup secondFlatItemsGroup = new FlatItemsGroup(FlatItemsGroupId.create(), Set.of(
                new FlatItem(missedFlatItemId, FlatItem.Side.Reverse),
                new FlatItem(FlatItemId.create(), FlatItem.Side.Reverse)
        ));

        MemoryGame memoryGame = new MemoryGame(
                memoryGameId,
                Set.of(
                        currentFlatItemsGroup,
                        secondFlatItemsGroup
                ),
                Set.of(),
                currentFlatItemsGroup
        );

        Optional<MemoryGameEvent> result = memoryGame.turnCard(missedFlatItemId);

        assertInstanceOf(MemoryGameEvent.Missed.class, result.get());
    }

    List<FlatItemsGroupId> extractFlatItemGroupIds(List<MemoryGameEvent> events) {
        return events.stream()
                .map(MemoryGameEvent::events)
                .flatMap(Collection::stream)
                .map(FlatItemsGroupEvent::flatItemsGroupId)
                .collect(Collectors.toUnmodifiableSet())
                .stream().toList();
    }

    List<FlatItemId> extractFlatItemIdsFor(FlatItemsGroupId flatItemsGroupId, List<MemoryGameEvent> events) {
        return events.stream()
                .map(MemoryGameEvent::events)
                .flatMap(Collection::stream)
                .filter(event -> event.flatItemsGroupId().equals(flatItemsGroupId))
                .map(FlatItemsGroupEvent::events)
                .flatMap(Collection::stream)
                .map(FlatItemEvent::flatItemId)
                .collect(Collectors.toUnmodifiableSet())
                .stream().toList();
    }

}