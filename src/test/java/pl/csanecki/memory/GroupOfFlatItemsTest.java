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

}