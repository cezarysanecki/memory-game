package pl.csanecki.memory.engine;

import org.junit.jupiter.api.Test;
import pl.csanecki.memory.GuessResult;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.csanecki.memory.GuessResult.*;

class MemoryGameTest {

    FlatItemId firstFlatItemId = FlatItemId.of(1);
    FlatItemId secondFlatItemId = FlatItemId.of(2);
    FlatItemId thirdFlatItemId = FlatItemId.of(3);
    FlatItemId fourthFlatItemId = FlatItemId.of(4);
    FlatItemId fifthFlatItemId = FlatItemId.of(5);

    @Test
    void properly_guessed_cards() {
        MemoryGame memoryGame = new MemoryGame(Set.of(
                GroupOfFlatItems.allReversed(Set.of(firstFlatItemId, secondFlatItemId)),
                GroupOfFlatItems.allReversed(Set.of(thirdFlatItemId, fourthFlatItemId))));

        GuessResult firstGuess = memoryGame.turnCard(firstFlatItemId);

        assertEquals(Continue, firstGuess);

        GuessResult secondGuess = memoryGame.turnCard(secondFlatItemId);

        assertEquals(Guessed, secondGuess);
    }

    @Test
    void need_to_reveal_all_cards_from_group() {
        MemoryGame memoryGame = new MemoryGame(Set.of(
                GroupOfFlatItems.allReversed(Set.of(firstFlatItemId, secondFlatItemId)),
                GroupOfFlatItems.allReversed(Set.of(thirdFlatItemId, fourthFlatItemId, fifthFlatItemId))));

        memoryGame.turnCard(thirdFlatItemId);
        GuessResult result = memoryGame.turnCard(fourthFlatItemId);

        assertEquals(Continue, result);
    }

    @Test
    void fail_to_guess_all_cards_from_group() {
        MemoryGame memoryGame = new MemoryGame(Set.of(
                GroupOfFlatItems.allReversed(Set.of(firstFlatItemId, secondFlatItemId)),
                GroupOfFlatItems.allReversed(Set.of(thirdFlatItemId, fourthFlatItemId))));

        memoryGame.turnCard(thirdFlatItemId);

        GuessResult result = memoryGame.turnCard(firstFlatItemId);

        assertEquals(Failure, result);
    }

    @Test
    void reset_revealed_cars_after_wrong_guess() {
        MemoryGame memoryGame = new MemoryGame(Set.of(
                GroupOfFlatItems.allReversed(Set.of(firstFlatItemId, secondFlatItemId)),
                GroupOfFlatItems.allReversed(Set.of(thirdFlatItemId, fourthFlatItemId))));

        memoryGame.turnCard(firstFlatItemId);

        memoryGame.turnCard(thirdFlatItemId);

        GuessResult result = memoryGame.turnCard(secondFlatItemId);

        assertEquals(Continue, result);
    }

    @Test
    void can_continue_to_guessing_next_cards() {
        MemoryGame memoryGame = new MemoryGame(Set.of(
                GroupOfFlatItems.allReversed(Set.of(firstFlatItemId, secondFlatItemId)),
                GroupOfFlatItems.allReversed(Set.of(thirdFlatItemId, fourthFlatItemId))));

        memoryGame.turnCard(firstFlatItemId);
        memoryGame.turnCard(secondFlatItemId);

        GuessResult result = memoryGame.turnCard(thirdFlatItemId);

        assertEquals(Continue, result);
    }

    @Test
    void end_game() {
        MemoryGame memoryGame = new MemoryGame(Set.of(
                GroupOfFlatItems.allReversed(Set.of(firstFlatItemId, secondFlatItemId)),
                GroupOfFlatItems.allReversed(Set.of(thirdFlatItemId, fourthFlatItemId))));

        memoryGame.turnCard(firstFlatItemId);
        memoryGame.turnCard(secondFlatItemId);
        memoryGame.turnCard(thirdFlatItemId);
        GuessResult result = memoryGame.turnCard(fourthFlatItemId);

        assertEquals(GameOver, result);
    }


}