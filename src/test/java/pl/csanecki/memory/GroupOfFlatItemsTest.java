package pl.csanecki.memory;

import org.junit.jupiter.api.Test;

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
    void cannot_create_group_of_items_with_one_item() {
        assertThrows(IllegalStateException.class,
                () -> GroupOfFlatItems.allReversed(Set.of(firstFlatItemId)));
    }

}