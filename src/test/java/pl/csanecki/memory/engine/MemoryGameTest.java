package pl.csanecki.memory.engine;

import org.junit.jupiter.api.Test;
import pl.csanecki.memory.GuessResult;
import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.GroupOfFlatItems;
import pl.csanecki.memory.engine.MemoryGame;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.csanecki.memory.GuessResult.*;

class MemoryGameTest {

    FlatItemId firstFlatItemId = FlatItemId.of(1);
    FlatItemId secondFlatItemId = FlatItemId.of(2);
    FlatItemId thirdFlatItemId = FlatItemId.of(3);
    FlatItemId fourthFlatItemId = FlatItemId.of(4);
    FlatItemId fifthFlatItemId = FlatItemId.of(5);

    GroupOfFlatItems firstGroupOfFlatItems = GroupOfFlatItems.allReversed(Set.of(
            firstFlatItemId, secondFlatItemId));
    GroupOfFlatItems secondGroupOfFlatItems = GroupOfFlatItems.allReversed(Set.of(
            thirdFlatItemId, fourthFlatItemId, fifthFlatItemId));

    @Test
    void properly_guessed_cards() {
        MemoryGame memoryGame = new MemoryGame(Set.of(firstGroupOfFlatItems, secondGroupOfFlatItems));

        GuessResult firstGuess = memoryGame.turnCard(firstFlatItemId);

        assertEquals(firstGuess, Continue);

        GuessResult secondGuess = memoryGame.turnCard(secondFlatItemId);

        assertEquals(secondGuess, Guessed);
    }

    @Test
    void need_to_reveal_all_cards_from_group() {
        MemoryGame memoryGame = new MemoryGame(Set.of(firstGroupOfFlatItems, secondGroupOfFlatItems));

        memoryGame.turnCard(thirdFlatItemId);
        GuessResult result = memoryGame.turnCard(fourthFlatItemId);

        assertEquals(result, Continue);
    }

    @Test
    void fail_to_guess_all_cards_from_group() {
        MemoryGame memoryGame = new MemoryGame(Set.of(firstGroupOfFlatItems, secondGroupOfFlatItems));

        memoryGame.turnCard(thirdFlatItemId);
        memoryGame.turnCard(fourthFlatItemId);

        GuessResult result = memoryGame.turnCard(firstFlatItemId);

        assertEquals(result, Failure);
    }

    @Test
    void reset_revealed_cars_after_wrong_guess() {
        MemoryGame memoryGame = new MemoryGame(Set.of(firstGroupOfFlatItems, secondGroupOfFlatItems));

        memoryGame.turnCard(thirdFlatItemId);
        memoryGame.turnCard(fourthFlatItemId);

        memoryGame.turnCard(firstFlatItemId);

        GuessResult result = memoryGame.turnCard(thirdFlatItemId);

        assertEquals(result, Continue);
    }



}