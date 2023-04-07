package pl.csanecki.memory.engine;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FlatItemsGroupTest {

    FlatItemsGroupId flatItemsGroupId = FlatItemsGroupId.of(0);

    FlatItemId firstFlatItemId = FlatItemId.of(0);
    FlatItemId secondFlatItemId = FlatItemId.of(1);

    @Test
    void all_are_reverse_up() {
        FlatItemsGroup flatItemsGroup = FlatItemsGroup.allReversed(
                flatItemsGroupId,
                Set.of(firstFlatItemId, secondFlatItemId));

        assertTrue(flatItemsGroup.isAllReverseUp());
    }

    @Test
    void all_are_not_obverse_up_and_reserve_up_when_one_of_items_is_obverse_up_from_all() {
        FlatItemsGroup flatItemsGroup = FlatItemsGroup.allReversed(
                flatItemsGroupId,
                Set.of(firstFlatItemId, secondFlatItemId));

        flatItemsGroup.turnToObverse(firstFlatItemId);

        assertFalse(flatItemsGroup.isAllReverseUp());
        assertFalse(flatItemsGroup.isAllObverseUp());
    }

    @Test
    void turning_all_items_make_them_be_obverse_up() {
        FlatItemsGroup flatItemsGroup = FlatItemsGroup.allReversed(
                flatItemsGroupId,
                Set.of(firstFlatItemId, secondFlatItemId));

        flatItemsGroup.turnToObverse(firstFlatItemId);
        flatItemsGroup.turnToObverse(secondFlatItemId);

        assertTrue(flatItemsGroup.isAllObverseUp());
    }

    @Test
    void turning_all_cards_to_be_reverse_up() {
        FlatItemsGroup flatItemsGroup = FlatItemsGroup.allReversed(
                flatItemsGroupId,
                Set.of(firstFlatItemId, secondFlatItemId));

        flatItemsGroup.turnToObverse(firstFlatItemId);
        flatItemsGroup.turnAllToReverseUp();

        assertTrue(flatItemsGroup.isAllReverseUp());
    }

    @Test
    void group_of_items_should_have_at_least_one_element() {
        assertThrows(IllegalStateException.class,
                () -> FlatItemsGroup.allReversed(flatItemsGroupId, Set.of()));
    }

    @Test
    void cannot_turn_card_not_being_in_group() {
        FlatItemId notConsideredFlatItem = FlatItemId.of(2);

        FlatItemsGroup flatItemsGroup = FlatItemsGroup.allReversed(
                flatItemsGroupId,
                Set.of(firstFlatItemId, secondFlatItemId));

        assertThrows(IllegalStateException.class,
                () -> flatItemsGroup.turnToObverse(notConsideredFlatItem));
    }

}