package pl.csanecki.memory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FlatItemTest {

    @Test
    void turning_around_reverse_up_makes_it_averse_up() {
        FlatItem flatItem = FlatItem.reverseUp(FlatItemId.of(1));

        flatItem.turnAround();

        assertTrue(flatItem.isAverseUp());
    }

    @Test
    void turning_around_averse_up_makes_it_reverse_up() {
        FlatItem flatItem = FlatItem.averseUp(FlatItemId.of(1));

        flatItem.turnAround();

        assertTrue(flatItem.isReverseUp());
    }

    @Test
    void turn_averse_up_no_matter_what() {
        FlatItem flatItem = FlatItem.reverseUp(FlatItemId.of(1));

        flatItem.turnAverseUp();

        assertTrue(flatItem.isAverseUp());
    }

    @Test
    void turn_reverse_up_no_matter_what() {
        FlatItem flatItem = FlatItem.averseUp(FlatItemId.of(1));

        flatItem.turnReverseUp();

        assertTrue(flatItem.isReverseUp());
    }
}