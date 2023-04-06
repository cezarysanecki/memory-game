package pl.csanecki.memory.state;

import pl.csanecki.memory.engine.FlatItemId;

public record FlatItemCurrentState(FlatItemId flatItemId, boolean averse) {
}
