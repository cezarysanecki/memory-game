package pl.csanecki.memory.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.csanecki.memory.engine.state.FlatItemCurrentState;
import pl.csanecki.memory.engine.state.GroupOfFlatItemsCurrentState;
import pl.csanecki.memory.engine.state.MemoryGameCurrentState;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static pl.csanecki.memory.engine.GuessResult.*;

class MemoryGameTest {

    @ParameterizedTest
    @CsvSource({"7,2", "2,7"})
    void cannot_create_game_for_not_dividable_number_of_cards_by_cards_in_group(
            int numberOfCards, int cardsInGroup
    ) {
        assertThrows(IllegalArgumentException.class,
                () -> MemoryGame.create(numberOfCards, cardsInGroup));
    }

    @ParameterizedTest
    @CsvSource({"-16,8", "4,-2","0,2"})
    void cannot_create_game_for_not_positive_values(
            int numberOfCards, int cardsInGroup
    ) {
        assertThrows(IllegalArgumentException.class,
                () -> MemoryGame.create(numberOfCards, cardsInGroup));
    }

    @Test
    void turning_first_card_from_group_informs_to_continue_guessing() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstGroup(currentState);
        FlatItemCurrentState firstFlatItemOfFirstGroup = getFirstFlatItem(firstGroupOfFlatItems);

        GuessResult result = memoryGame.turnCard(firstFlatItemOfFirstGroup.flatItemId());

        assertEquals(Continue, result);
    }

    @Test
    void guessing_all_cards_in_group_requires_turning_them_all_in_row() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstGroup(currentState);
        Set<FlatItemCurrentState> flatItems = firstGroupOfFlatItems.flatItems();

        flatItems.stream()
                .skip(1)
                .forEach(flatItem -> memoryGame.turnCard(flatItem.flatItemId()));

        FlatItemCurrentState firstFlatItemOfFirstGroup = getFirstFlatItem(firstGroupOfFlatItems);
        GuessResult result = memoryGame.turnCard(firstFlatItemOfFirstGroup.flatItemId());

        assertEquals(Guessed, result);
    }

    @Test
    void not_guessing_all_cards_in_group_in_row_determines_failure() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstGroup(currentState);
        FlatItemCurrentState firstFlatItemOfFirstGroup = getFirstFlatItem(firstGroupOfFlatItems);
        memoryGame.turnCard(firstFlatItemOfFirstGroup.flatItemId());

        GroupOfFlatItemsCurrentState secondGroupOfFlatItems = getSecondGroup(currentState);
        FlatItemCurrentState secondFlatItemOfFirstGroup = getFirstFlatItem(secondGroupOfFlatItems);
        GuessResult result = memoryGame.turnCard(secondFlatItemOfFirstGroup.flatItemId());

        assertEquals(Failure, result);
    }

    @Test
    void next_turn_after_failure_informs_to_continue_guessing() {
        MemoryGame memoryGame = MemoryGame.create(4, 2);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstGroup(currentState);
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

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstGroup(currentState);
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

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstGroup(currentState);
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

        GroupOfFlatItemsCurrentState firstGroupOfFlatItems = getFirstGroup(currentState);
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

    private static GroupOfFlatItemsCurrentState getFirstGroup(MemoryGameCurrentState currentState) {
        return currentState.groupOfFlatItems()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("should contain at least one group"));
    }

    private static GroupOfFlatItemsCurrentState getSecondGroup(MemoryGameCurrentState currentState) {
        return currentState.groupOfFlatItems()
                .stream()
                .skip(1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("should contain at least two groups"));
    }

    private static FlatItemCurrentState getFirstFlatItem(GroupOfFlatItemsCurrentState groupOfFlatItems) {
        return groupOfFlatItems.flatItems()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("should contain at least one card in group"));
    }


}