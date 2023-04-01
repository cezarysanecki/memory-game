package pl.csanecki.memory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FlatItemTest {

    @Test
    void by_default_flat_item_is_placed_reverse_side() {
        FlatItem flatItem = new FlatItem(CardId.of(1));

        assertTrue(flatItem.isReverseSided());
    }

    @Test
    void first_turn_around_of_flat_item_placed_it_averse_side() {
        FlatItem flatItem = new FlatItem(CardId.of(1));

        flatItem.turnCard();

        assertTrue(flatItem.isAverseSided());
    }
}