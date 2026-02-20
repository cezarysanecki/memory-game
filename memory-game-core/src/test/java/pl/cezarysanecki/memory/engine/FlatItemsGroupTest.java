package pl.cezarysanecki.memory.engine;

import org.junit.jupiter.api.Test;
import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cezarysanecki.memory.engine.FlatItem.Side.Obverse;
import static pl.cezarysanecki.memory.engine.FlatItem.Side.Reverse;

class FlatItemsGroupTest {
    @Test
    void all_are_reverse_up_after_creation() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Reverse);

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));

        assertTrue(flatItemsGroup.flatItems().stream().allMatch(flatItem -> flatItem.side() == Reverse));
    }

    @Test
    void all_are_obverse_up_after_creation() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Obverse);

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));

        assertTrue(flatItemsGroup.flatItems().stream().allMatch(flatItem -> flatItem.side() == Obverse));
    }

    @Test
    void can_turn_up_card_to_obverse_if_on_reverse_side() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Reverse);
        List<FlatItemId> flatItemIds = extractFlatItemIds(List.of(event));

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));
        Optional<FlatItemsGroupEvent> newEvent = flatItemsGroup.turnUpTo(flatItemIds.getFirst(), Obverse);

        assertInstanceOf(FlatItemsGroupEvent.FlatItemTurned.class, newEvent.get());
    }

    @Test
    void can_turn_up_card_to_reverse_if_on_obverse_side() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Obverse);
        List<FlatItemId> flatItemIds = extractFlatItemIds(List.of(event));

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));
        Optional<FlatItemsGroupEvent> newEvent = flatItemsGroup.turnUpTo(flatItemIds.getFirst(), Reverse);

        assertInstanceOf(FlatItemsGroupEvent.FlatItemTurned.class, newEvent.get());
    }

    @Test
    void do_nothing_when_turning_up_card_to_obverse_if_is_already_on_obverse() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Obverse);
        List<FlatItemId> flatItemIds = extractFlatItemIds(List.of(event));

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));
        Optional<FlatItemsGroupEvent> newEvent = flatItemsGroup.turnUpTo(flatItemIds.getFirst(), Obverse);

        assertTrue(newEvent.isEmpty());
    }

    @Test
    void do_nothing_when_turning_up_card_to_reverse_if_is_already_on_reverse() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Reverse);
        List<FlatItemId> flatItemIds = extractFlatItemIds(List.of(event));

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));
        Optional<FlatItemsGroupEvent> newEvent = flatItemsGroup.turnUpTo(flatItemIds.getFirst(), Reverse);

        assertTrue(newEvent.isEmpty());
    }

    @Test
    void turn_all_cards_to_obverse_if_on_reverse_side() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Reverse);

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));
        Optional<FlatItemsGroupEvent> newEvent = flatItemsGroup.turnUpAllTo(Obverse);

        assertInstanceOf(FlatItemsGroupEvent.AllTurnedObverse.class, newEvent.get());
    }

    @Test
    void turn_all_cards_to_reverse_if_on_observe_side() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Obverse);

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));
        Optional<FlatItemsGroupEvent> newEvent = flatItemsGroup.turnUpAllTo(Reverse);

        assertInstanceOf(FlatItemsGroupEvent.AllTurnedReverse.class, newEvent.get());
    }

    @Test
    void do_nothing_when_turning_up_all_cards_to_obverse_if_are_already_on_obverse() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Obverse);

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));
        Optional<FlatItemsGroupEvent> newEvent = flatItemsGroup.turnUpAllTo(Obverse);

        assertTrue(newEvent.isEmpty());
    }

    @Test
    void do_nothing_when_turning_up_all_cards_to_reverse_if_are_already_on_reverse() {
        FlatItemsGroupEvent event = FlatItemsGroup.create(4, Reverse);

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.restore(List.of(event));
        Optional<FlatItemsGroupEvent> newEvent = flatItemsGroup.turnUpAllTo(Reverse);

        assertTrue(newEvent.isEmpty());
    }

    List<FlatItemId> extractFlatItemIds(List<FlatItemsGroupEvent> events) {
        return events.stream()
                .map(FlatItemsGroupEvent::events)
                .flatMap(Collection::stream)
                .map(FlatItemEvent::flatItemId)
                .collect(Collectors.toUnmodifiableSet())
                .stream().toList();
    }

}