package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.FlatItemEvent.Initialized;
import pl.cezarysanecki.memory.engine.FlatItemEvent.TurnedObverseUp;
import pl.cezarysanecki.memory.engine.FlatItemEvent.TurnedReverseUp;
import pl.cezarysanecki.memory.engine.api.FlatItemId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.comparing;

final class FlatItem {

    private final FlatItemId flatItemId;
    private Side side;

    FlatItem(FlatItemId flatItemId, Side side) {
        this.flatItemId = flatItemId;
        this.side = side;
    }

    enum Side {
        Reverse, Obverse
    }

    static FlatItem restore(List<FlatItemEvent> events) {
        Initialized initializedEvent = events.stream()
                .filter(event -> event instanceof Initialized)
                .map(Initialized.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("cannot restore flat item without initialization event"));

        FlatItem flatItem = new FlatItem(initializedEvent.flatItemId(), Side.Reverse);
        events.stream().sorted(comparing(FlatItemEvent::when)).forEach(flatItem::apply);
        return flatItem;
    }

    static List<FlatItemEvent> create(Side side) {
        FlatItemId flatItemId = FlatItemId.create();

        final Initialized initialized = new Initialized(flatItemId);

        return switch (side) {
            case Obverse -> List.of(initialized, new TurnedObverseUp(flatItemId));
            case Reverse -> List.of(initialized, new TurnedReverseUp(flatItemId));
        };
    }

    FlatItemEvent flip() {
        return isUp(Side.Obverse) ? new TurnedReverseUp(flatItemId) : new TurnedObverseUp(flatItemId);
    }

    Optional<FlatItemEvent> turnUpTo(Side side) {
        if (isUp(side)) {
            return Optional.empty();
        }

        return switch (side) {
            case Obverse -> Optional.of(new TurnedObverseUp(flatItemId));
            case Reverse -> Optional.of(new TurnedReverseUp(flatItemId));
        };
    }

    private boolean isUp(Side side) {
        return this.side == side;
    }

    private void apply(FlatItemEvent event) {
        if (event instanceof TurnedObverseUp) {
            this.side = Side.Obverse;
        } else if (event instanceof TurnedReverseUp) {
            this.side = Side.Reverse;
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

    public FlatItemId flatItemId() {
        return flatItemId;
    }

    public Side side() {
        return side;
    }

    @Override
    public String toString() {
        return "FlatItem[" +
                "flatItemId=" + flatItemId + ", " +
                "side=" + side + ']';
    }


}