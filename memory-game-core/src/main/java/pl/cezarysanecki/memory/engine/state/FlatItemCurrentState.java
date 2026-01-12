package pl.cezarysanecki.memory.engine.state;

import pl.cezarysanecki.memory.engine.FlatItemId;

public record FlatItemCurrentState(FlatItemId flatItemId, boolean obverse) {
}
