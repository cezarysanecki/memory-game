package pl.cezarysanecki.memory.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;
import pl.cezarysanecki.memory.engine.api.GuessResult;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.Continue;
import static pl.cezarysanecki.memory.engine.api.GuessResult.State.Failure;
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
        MemoryGameState gameState = memoryGame.state();

        MemoryGameState.FlatItem firstFlatItem = getFirstFlatItem(gameState);

        GuessResult result = memoryGame.turnCard(firstFlatItem.flatItemId());

        assertEquals(Continue, result.actionResult());
    }

    @Test
    void guessing_all_cards_in_group_requires_turning_them_all_in_row() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        MemoryGameState gameState = memoryGame.state();

        Set<MemoryGameState.FlatItem> inSameGroup = getAllFlatItemsRelatedToFirstGroup(gameState);

        List<GuessResult.State> results = inSameGroup.stream()
                .map(flatItem -> memoryGame.turnCard(flatItem.flatItemId()))
                .map(GuessResult::actionResult)
                .toList();

        assertTrue(results.contains(Guessed));
    }

    @Test
    void not_guessing_all_cards_in_group_in_row_determines_failure() {
        MemoryGame memoryGame = MemoryGameFactory.create(4, 2);
        MemoryGameState gameState = memoryGame.state();

        Map<FlatItemsGroupId, List<MemoryGameState.FlatItem>> groupsToItems = groupAllFlatItems(gameState);

        List<MemoryGameState.FlatItem> itemsFromDifferentGroups = groupsToItems.values().stream()
                .limit(2)
                .map(List::getFirst)
                .toList();

        List<GuessResult.State> results = itemsFromDifferentGroups.stream()
                .map(flatItem -> memoryGame.turnCard(flatItem.flatItemId()))
                .map(GuessResult::actionResult)
                .toList();

        assertTrue(results.contains(Failure));
    }

    @Test
    void next_turn_after_failure_informs_to_continue_guessing() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstFlatItem(currentState);
        FlatItemCurrentState firstFlatItemOfFirstGroup = getFirstFlatItem(firstGroupOfFlatItems);
        memoryGame.turnCard(firstFlatItemOfFirstGroup.flatItemId());

        GroupOfFlatItemsCurrentState secondGroupOfFlatItems = getSecondGroup(currentState);
        FlatItemCurrentState secondFlatItemOfFirstGroup = getFirstFlatItem(secondGroupOfFlatItems);
        memoryGame.turnCard(secondFlatItemOfFirstGroup.flatItemId());

        GuessResult result = memoryGame.turnCard(firstFlatItemOfFirstGroup.flatItemId());

        assertEquals(Continue, result);
    }

    @Test
    void next_turn_after_guess_one_group_informs_to_continue_guessing() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstFlatItem(currentState);
        firstGroupOfFlatItems.flatItems()
                .forEach(flatItem -> memoryGame.turnCard(flatItem.flatItemId()));

        GroupOfFlatItemsCurrentState secondGroupOfFlatItems = getSecondGroup(currentState);
        FlatItemCurrentState secondFlatItemOfFirstGroup = getFirstFlatItem(secondGroupOfFlatItems);
        GuessResult result = memoryGame.turnCard(secondFlatItemOfFirstGroup.flatItemId());

        assertEquals(Continue, result);
    }

    @Test
    void guess_all_groups_ends_game() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);

        memoryGame.currentState()
                .groupOfFlatItems()
                .stream()
                .map(GroupOfFlatItemsCurrentState::flatItems)
                .flatMap(Collection::stream)
                .forEach(flatItem -> memoryGame.turnCard(flatItem.flatItemId()));

        MemoryGameCurrentState currentState = memoryGame.currentState();

        assertTrue(currentState.isFinished());
    }

    @Test
    void guess_cards_are_not_counting_as_mistake_when_try_to_turn_them() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstFlatItem(currentState);
        firstGroupOfFlatItems.flatItems()
                .forEach(flatItem -> memoryGame.turnCard(flatItem.flatItemId()));

        GroupOfFlatItemsCurrentState secondGroupOfFlatItems = getSecondGroup(currentState);
        FlatItemCurrentState secondFlatItemOfFirstGroup = getFirstFlatItem(secondGroupOfFlatItems);
        memoryGame.turnCard(secondFlatItemOfFirstGroup.flatItemId());

        FlatItemCurrentState firstFlatItemOfFirstGroup = getFirstFlatItem(firstGroupOfFlatItems);
        GuessResult result = memoryGame.turnCard(firstFlatItemOfFirstGroup.flatItemId());

        assertEquals(Continue, result);
    }

    @Test
    void reset_game_when_it_is_underway_makes_it_back_to_initial_state() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstFlatItem(currentState);
        firstGroupOfFlatItems.flatItems()
                .forEach(flatItem -> memoryGame.turnCard(flatItem.flatItemId()));

        boolean result = memoryGame.currentState()
                .groupOfFlatItems()
                .stream()
                .map(GroupOfFlatItemsCurrentState::flatItems)
                .flatMap(Collection::stream)
                .anyMatch(FlatItemCurrentState::obverse);
        assertTrue(result);

        memoryGame.reset();

        result = memoryGame.currentState()
                .groupOfFlatItems()
                .stream()
                .map(GroupOfFlatItemsCurrentState::flatItems)
                .flatMap(Collection::stream)
                .anyMatch(FlatItemCurrentState::obverse);
        assertFalse(result);
    }

    private static MemoryGameState.FlatItem getFirstFlatItem(MemoryGameState gameState) {
        return gameState.flatItems()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("should contain at least one group"));
    }

    private static Set<MemoryGameState.FlatItem> getAllFlatItemsRelatedToFirstGroup(MemoryGameState gameState) {
        return groupAllFlatItems(gameState)
                .values()
                .stream()
                .findFirst()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static Map<FlatItemsGroupId, List<MemoryGameState.FlatItem>> groupAllFlatItems(MemoryGameState gameState) {
        return gameState.flatItems().stream()
                .collect(Collectors.groupingBy(MemoryGameState.FlatItem::assignedGroupId));
    }
}