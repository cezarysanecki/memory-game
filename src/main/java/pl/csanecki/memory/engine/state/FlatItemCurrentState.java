package pl.csanecki.memory.engine.state;

import pl.csanecki.memory.engine.FlatItemId;

public record FlatItemCurrentState(FlatItemId flatItemId, boolean averse) {
}
