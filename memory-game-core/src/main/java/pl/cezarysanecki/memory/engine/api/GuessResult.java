package pl.cezarysanecki.memory.engine.api;

public record GuessResult(
        State actionResult,
        MemoryGameState state
) {

    public enum State {
        Continue, Guessed, GameOver, Failure
    }

}
