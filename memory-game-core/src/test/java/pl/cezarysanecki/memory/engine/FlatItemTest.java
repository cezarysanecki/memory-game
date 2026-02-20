package pl.cezarysanecki.memory.engine;

import org.junit.jupiter.api.Test;
import pl.cezarysanecki.memory.engine.api.FlatItemId;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cezarysanecki.memory.engine.FlatItem.Side.Obverse;
import static pl.cezarysanecki.memory.engine.FlatItem.Side.Reverse;

class FlatItemTest {

    private final FlatItemId flatItemId = FlatItemId.create();

    @Test
    void create_flat_item_with_reverse_side_up() {
        List<FlatItemEvent> events = FlatItem.create(Reverse);

        FlatItem flatItem = FlatItem.restore(events);

        assertEquals(Reverse, flatItem.side());
    }

    @Test
    void create_flat_item_with_obverse_side_up() {
        List<FlatItemEvent> events = FlatItem.create(Obverse);

        FlatItem flatItem = FlatItem.restore(events);

        assertEquals(Obverse, flatItem.side());
    }

    @Test
    void turning_around_reverse_up_makes_it_obverse_up() {
        FlatItem flatItem = new FlatItem(flatItemId, Reverse);

        Optional<FlatItemEvent> flatItemEvent = flatItem.turnUpTo(Obverse);

        assertInstanceOf(FlatItemEvent.TurnedObverseUp.class, flatItemEvent.get());
    }

    @Test
    void turning_around_obverse_up_makes_it_reverse_up() {
        FlatItem flatItem = new FlatItem(flatItemId, Obverse);

        Optional<FlatItemEvent> flatItemEvent = flatItem.turnUpTo(Reverse);

        assertInstanceOf(FlatItemEvent.TurnedReverseUp.class, flatItemEvent.get());
    }

    @Test
    void do_nothing_if_turning_reverse_side_being_reverse_side() {
        FlatItem flatItem = new FlatItem(flatItemId, Reverse);

        Optional<FlatItemEvent> flatItemEvent = flatItem.turnUpTo(Reverse);

        assertTrue(flatItemEvent.isEmpty());
    }

    @Test
    void do_nothing_if_turning_obverse_side_being_obverse_side() {
        FlatItem flatItem = new FlatItem(flatItemId, Obverse);

        Optional<FlatItemEvent> flatItemEvent = flatItem.turnUpTo(Obverse);

        assertTrue(flatItemEvent.isEmpty());
    }

    @Test
    void flip_flat_item_to_different_side() {
        FlatItem flatItem = aFlatItem(Obverse);

        FlatItemEvent flatItemEvent = flatItem.flip();

        assertInstanceOf(FlatItemEvent.TurnedReverseUp.class, flatItemEvent);
    }

    FlatItem aFlatItem(FlatItem.Side side) {
        List<FlatItemEvent> events = FlatItem.create(side);
        return FlatItem.restore(events);
    }
}