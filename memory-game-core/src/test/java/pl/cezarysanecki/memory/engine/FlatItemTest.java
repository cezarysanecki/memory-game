package pl.cezarysanecki.memory.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FlatItemTest {

    @Test
    void turning_around_reverse_up_makes_it_obverse_up() {
        FlatItem flatItem = FlatItem.reverseUp(FlatItemId.of(1));

        flatItem.flip();

        assertTrue(flatItem.isObverseUp());
    }

    @Test
    void turning_around_obverse_up_makes_it_reverse_up() {
        FlatItem flatItem = FlatItem.obverseUp(FlatItemId.of(1));

        flatItem.flip();

        assertTrue(flatItem.isReverseUp());
    }

    @Test
    void turn_obverse_up_no_matter_what() {
        FlatItem flatItem = FlatItem.reverseUp(FlatItemId.of(1));

        flatItem.turnObverseUp();

        assertTrue(flatItem.isObverseUp());
    }

    @Test
    void turn_reverse_up_no_matter_what() {
        FlatItem flatItem = FlatItem.obverseUp(FlatItemId.of(1));

        flatItem.turnReverseUp();

        assertTrue(flatItem.isReverseUp());
    }
}