package pl.csanecki.memory.engine;

import org.junit.jupiter.api.Test;
import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.GroupOfFlatItems;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GroupOfFlatItemsTest {

    FlatItemId firstFlatItemId = FlatItemId.of(0);
    FlatItemId secondFlatItemId = FlatItemId.of(1);

    @Test
    void all_are_reverse_up() {
        GroupOfFlatItems groupOfFlatItems = GroupOfFlatItems.allReversed(
                Set.of(firstFlatItemId, secondFlatItemId));

        assertTrue(groupOfFlatItems.isAllReverseUp());
    }

    @Test
    void all_are_not_averse_up_and_reserve_up_when_one_of_items_is_averse_up_from_all() {
        GroupOfFlatItems groupOfFlatItems = GroupOfFlatItems.allReversed(
                Set.of(firstFlatItemId, secondFlatItemId));

        groupOfFlatItems.turnToAverse(firstFlatItemId);

        assertFalse(groupOfFlatItems.isAllReverseUp());
        assertFalse(groupOfFlatItems.isAllAverseUp());
    }

    @Test
    void turning_all_items_make_them_be_averse_up() {
        GroupOfFlatItems groupOfFlatItems = GroupOfFlatItems.allReversed(
                Set.of(firstFlatItemId, secondFlatItemId));

        groupOfFlatItems.turnToAverse(firstFlatItemId);
        groupOfFlatItems.turnToAverse(secondFlatItemId);

        assertTrue(groupOfFlatItems.isAllAverseUp());
    }

    @Test
    void turning_all_cards_to_be_reverse_up() {
        GroupOfFlatItems groupOfFlatItems = GroupOfFlatItems.allReversed(
                Set.of(firstFlatItemId, secondFlatItemId));

        groupOfFlatItems.turnToAverse(firstFlatItemId);
        groupOfFlatItems.turnAllToReverseUp();

        assertTrue(groupOfFlatItems.isAllReverseUp());
    }

    @Test
    void group_of_items_should_have_at_least_one_element() {
        assertThrows(IllegalStateException.class,
                () -> GroupOfFlatItems.allReversed(Set.of()));
    }

    @Test
    void cannot_turn_card_not_being_in_group() {
        FlatItemId notConsideredFlatItem = FlatItemId.of(2);

        GroupOfFlatItems groupOfFlatItems = GroupOfFlatItems.allReversed(
                Set.of(firstFlatItemId, secondFlatItemId));

        assertThrows(IllegalStateException.class,
                () -> groupOfFlatItems.turnToAverse(notConsideredFlatItem));
    }

}