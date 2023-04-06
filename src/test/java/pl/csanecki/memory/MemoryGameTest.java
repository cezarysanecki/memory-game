package pl.csanecki.memory;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.csanecki.memory.GuessResult.Continue;
import static pl.csanecki.memory.GuessResult.Guessed;

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



}