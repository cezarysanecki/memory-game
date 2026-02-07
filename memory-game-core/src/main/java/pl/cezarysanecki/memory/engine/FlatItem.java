package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.FlatItemEvent.TurnedObverseUp;
import pl.cezarysanecki.memory.engine.FlatItemEvent.TurnedReverseUp;
import pl.cezarysanecki.memory.engine.api.FlatItemId;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.comparing;

final class FlatItem {

    private enum Side {
        Reverse, Obverse
    }

    private final FlatItemId flatItemId;
    private Side side;

    private FlatItem(FlatItemId flatItemId, Side side) {
        this.flatItemId = flatItemId;
        this.side = side;
    }

    static FlatItem restore(FlatItemId flatItemId, boolean obverseUp) {
        return new FlatItem(flatItemId, obverseUp ? Side.Obverse : Side.Reverse);
    }

    static FlatItem obverseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.Obverse);
    }

    static FlatItem reverseUp(FlatItemId flatItemId) {
        return new FlatItem(flatItemId, Side.Reverse);
    }

    static FlatItem restore(FlatItemId flatItemId, Collection<FlatItemEvent> events) {
        if (events.isEmpty()) {
            throw new IllegalArgumentException("events collection cannot be empty");
        }
        if (!events.stream().allMatch(event -> event.flatItemId().equals(flatItemId))) {
            throw new IllegalArgumentException("all events must have the same flat item id");
        }
        FlatItem flatItem = reverseUp(flatItemId);
        events.stream().sorted(comparing(FlatItemEvent::when)).forEach(flatItem::apply);
        return flatItem;
    }

    FlatItemEvent flip() {
        return side == Side.Obverse ? new TurnedReverseUp(flatItemId) : new TurnedObverseUp(flatItemId);
    }

    Optional<FlatItemEvent> turnObverseUp() {
        if (side == Side.Obverse) {
            return Optional.empty();
        }
        return Optional.of(new TurnedObverseUp(flatItemId));
    }

    Optional<FlatItemEvent> turnReverseUp() {
        if (side == Side.Reverse) {
            return Optional.empty();
        }
        return Optional.of(new TurnedReverseUp(flatItemId));
    }

    boolean isObverseUp() {
        return side == Side.Obverse;
    }

    boolean isReverseUp() {
        return side == Side.Reverse;
    }

    FlatItemId getFlatItemId() {
        return flatItemId;
    }

    private void apply(FlatItemEvent event) {
        if (event instanceof TurnedObverseUp) {
            turnObverseUp();
        } else if (event instanceof TurnedReverseUp) {
            turnReverseUp();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatItem flatItem = (FlatItem) o;
        return Objects.equals(flatItemId, flatItem.flatItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatItemId);
    }

}