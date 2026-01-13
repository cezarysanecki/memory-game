package pl.cezarysanecki.memory.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;
import pl.cezarysanecki.memory.engine.api.GuessResult;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.Continue;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.Failure;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.GameOver;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.Guessed;

class MemoryGameTest {

    @ParameterizedTest
    @CsvSource({"7,2", "2,7"})
    void cannot_create_game_for_not_dividable_number_of_cards_by_cards_in_group(
            int numberOfCards, int cardsInGroup
    ) {
        assertThrows(IllegalArgumentException.class,
                () -> MemoryGameFactory.create(numberOfCards, cardsInGroup));
    }

    @ParameterizedTest
    @CsvSource({"-16,8", "4,-2", "0,2"})
    void cannot_create_game_for_not_positive_values(
            int numberOfCards, int cardsInGroup
    ) {
        assertThrows(IllegalArgumentException.class,
                () -> MemoryGameFactory.create(numberOfCards, cardsInGroup));
    }

    @Test
    void turning_first_card_from_group_informs_to_continue_guessing() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        TestMemoryGameQuery testMemoryGameQuery = new TestMemoryGameQuery(memoryGame.state());

        MemoryGameState.FlatItem firstFlatItem = testMemoryGameQuery.anyFlatItem();

        GuessResult result = memoryGame.turnCard(firstFlatItem.flatItemId());

        assertEquals(Continue, result.actionResult());
    }

    @Test
    void guessing_all_cards_in_group_requires_turning_them_all_in_row() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        TestMemoryGameQuery testMemoryGameQuery = new TestMemoryGameQuery(memoryGame.state());

        List<MemoryGameState.FlatItem> inSameGroup = testMemoryGameQuery.allFlatItemsFromAnyGroup();

        List<GuessResult.State> results = inSameGroup.stream()
                .map(flatItem -> memoryGame.turnCard(flatItem.flatItemId()))
                .map(GuessResult::actionResult)
                .toList();

        assertTrue(results.contains(Guessed));
    }

    @Test
    void not_guessing_all_cards_in_group_in_row_determines_failure() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        TestMemoryGameQuery testMemoryGameQuery = new TestMemoryGameQuery(memoryGame.state());

        List<MemoryGameState.FlatItem> itemsFromDifferentGroups = testMemoryGameQuery.firstFlatItemsFromGroups(2);

        List<GuessResult.State> results = itemsFromDifferentGroups.stream()
                .map(flatItem -> memoryGame.turnCard(flatItem.flatItemId()))
                .map(GuessResult::actionResult)
                .toList();

        assertTrue(results.contains(Failure));
    }

    @Test
    void next_turn_after_failure_informs_to_continue_guessing() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        TestMemoryGameQuery testMemoryGameQuery = new TestMemoryGameQuery(memoryGame.state());

        List<MemoryGameState.FlatItem> itemsFromDifferentGroups = testMemoryGameQuery.firstFlatItemsFromGroups(2);
        itemsFromDifferentGroups.forEach(flatItem -> memoryGame.turnCard(flatItem.flatItemId()));

        GuessResult result = memoryGame.turnCard(itemsFromDifferentGroups.getFirst().flatItemId());
        assertEquals(Continue, result.actionResult());
    }

    @Test
    void next_turn_after_guess_one_group_informs_to_continue_guessing() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        TestMemoryGameQuery testMemoryGameQuery = new TestMemoryGameQuery(memoryGame.state());

        List<MemoryGameState.FlatItem> allFromSameGroup = testMemoryGameQuery.allFlatItemsFromAnyGroup();
        List<GuessResult.State> results = allFromSameGroup.stream()
                .map(flatItem -> memoryGame.turnCard(flatItem.flatItemId()))
                .map(GuessResult::actionResult)
                .toList();

        assertTrue(results.contains(Continue));
    }

    @Test
    void guess_all_groups_ends_game() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        TestMemoryGameQuery testMemoryGameQuery = new TestMemoryGameQuery(memoryGame.state());

        Map<FlatItemsGroupId, List<MemoryGameState.FlatItem>> groupedAllFlatItems = testMemoryGameQuery.groupedAllFlatItems();
        List<GuessResult.State> results = groupedAllFlatItems.values()
                .stream()
                .flatMap(flatItems -> flatItems
                        .stream()
                        .map(flatItem -> memoryGame.turnCard(flatItem.flatItemId())))
                .map(GuessResult::actionResult)
                .toList();

        assertTrue(results.contains(GameOver));
    }

    @Test
    void guess_cards_are_not_counting_as_mistake_when_try_to_turn_them() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        TestMemoryGameQuery testMemoryGameQuery = new TestMemoryGameQuery(memoryGame.state());

        Map<FlatItemsGroupId, List<MemoryGameState.FlatItem>> groupedAllFlatItems = testMemoryGameQuery.groupedAllFlatItems();
        List<FlatItemsGroupId> flatItemsGroupIds = new ArrayList<>(groupedAllFlatItems.keySet());

        FlatItemsGroupId firstGroupId = flatItemsGroupIds.get(0);
        FlatItemsGroupId secondGroupId = flatItemsGroupIds.get(1);

        groupedAllFlatItems.get(firstGroupId)
                .forEach(flatItem -> memoryGame.turnCard(flatItem.flatItemId()));

        memoryGame.turnCard(groupedAllFlatItems.get(secondGroupId).getFirst().flatItemId());

        GuessResult result = memoryGame.turnCard(groupedAllFlatItems.get(firstGroupId).getFirst().flatItemId());
        assertEquals(Continue, result.actionResult());
    }

    private record TestMemoryGameQuery(
            MemoryGameState gameState
    ) {

        private MemoryGameState.FlatItem anyFlatItem() {
            return gameState.flatItems()
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("should contain at least one group"));
        }

        private List<MemoryGameState.FlatItem> allFlatItemsFromAnyGroup() {
            return groupedAllFlatItems()
                    .values()
                    .stream()
                    .findAny()
                    .stream()
                    .flatMap(Collection::stream)
                    .toList();
        }

        private Map<FlatItemsGroupId, List<MemoryGameState.FlatItem>> groupedAllFlatItems() {
            return gameState.flatItems().stream()
                    .collect(Collectors.groupingBy(MemoryGameState.FlatItem::assignedGroupId));
        }

        private List<MemoryGameState.FlatItem> firstFlatItemsFromGroups(int numberOfGroups) {
            return groupedAllFlatItems().values().stream()
                    .limit(numberOfGroups)
                    .map(List::getFirst)
                    .toList();
        }

    }

}