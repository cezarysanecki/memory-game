package pl.csanecki.memory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FlatItemTest {

    @Test
    void by_default_flat_item_is_placed_reverse_side() {
        FlatItem flatItem = new FlatItem(FlatItemId.of(1));

        assertTrue(flatItem.isReverseSided());
    }

    @Test
    void first_turn_around_of_flat_item_placed_it_averse_side() {
        FlatItem flatItem = new FlatItem(FlatItemId.of(1));

        flatItem.turnCard();

        assertTrue(flatItem.isAverseSided());
    }

    @Test
    void turn_to_averse_no_matter_what() {
        FlatItem flatItem = new FlatItem(FlatItemId.of(1));

        flatItem.turnToAverse();

        assertTrue(flatItem.isAverseSided());
    }

    @Test
    void turn_to_reverse_no_matter_what() {
        FlatItem flatItem = new FlatItem(FlatItemId.of(1));
        flatItem.turnToAverse();

        flatItem.turnToReverse();

        assertTrue(flatItem.isReverseSided());
    }
}