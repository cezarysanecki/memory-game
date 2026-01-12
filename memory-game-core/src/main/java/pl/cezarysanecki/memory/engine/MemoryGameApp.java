package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.GuessResult;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;

public class MemoryGameApp {

    private final MemoryGameRepository memoryGameRepository;

    public MemoryGameApp(MemoryGameRepository memoryGameRepository) {
        this.memoryGameRepository = memoryGameRepository;
    }

    public MemoryGameId start(int numberOfCards, int cardsInGroup) {
        MemoryGameState game = MemoryGameFactory.create(numberOfCards, cardsInGroup);

        memoryGameRepository.save(game);

        return game.memoryGameId();
    }

    public GuessResult turnCard(MemoryGameId memoryGameId, FlatItemId flatItemId) {
        MemoryGameState gameState = memoryGameRepository.load(memoryGameId);
        MemoryGame game = MemoryGameFactory.restore(gameState);

        GuessResult guessResult = game.turnCard(flatItemId);

        MemoryGameState newState = gameState.applyResult(flatItemId, guessResult);
        memoryGameRepository.save(newState);

        return guessResult;
    }

    public MemoryGameState getState(MemoryGameId memoryGameId) {
        return memoryGameRepository.load(memoryGameId);
    }

}
