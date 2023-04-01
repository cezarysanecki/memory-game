package pl.csanecki.memory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FlatItemTest {

    @Test
    void turning_around_reverse_side_makes_it_averse_side() {
        FlatItem flatItem = FlatItem.reverse(FlatItemId.of(1));

        flatItem.turnCard();

        assertTrue(flatItem.isAverseSided());
    }

    @Test
    void turning_around_averse_side_makes_it_reverse_side() {
        FlatItem flatItem = FlatItem.averse(FlatItemId.of(1));

        flatItem.turnCard();

        assertTrue(flatItem.isReverseSided());
    }

    @Test
    void turn_to_averse_no_matter_what() {
        FlatItem flatItem = FlatItem.reverse(FlatItemId.of(1));

        flatItem.turnToAverse();

        assertTrue(flatItem.isAverseSided());
    }

    @Test
    void turn_to_reverse_no_matter_what() {
        FlatItem flatItem = FlatItem.averse(FlatItemId.of(1));

        flatItem.turnToReverse();

        assertTrue(flatItem.isReverseSided());
    }
}