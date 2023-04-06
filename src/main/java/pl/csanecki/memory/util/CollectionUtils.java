package pl.csanecki.memory.util;

import java.util.HashSet;
import java.util.List;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean containsDuplicates(List<?> list) {
        return !list.stream()
                .allMatch(new HashSet<>()::add);
    }
}
