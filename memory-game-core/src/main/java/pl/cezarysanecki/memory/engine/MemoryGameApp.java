package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.GuessResult;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;
import pl.cezarysanecki.memory.engine.db.MemoryGameRepository;

public class MemoryGameApp {

    private final MemoryGameRepository memoryGameRepository;

    public MemoryGameApp(MemoryGameRepository memoryGameRepository) {
        this.memoryGameRepository = memoryGameRepository;
    }

    public static MemoryGameApp inMemory() {
        return new MemoryGameApp(new InMemoryMemoryGameRepository());
    }

    public MemoryGameState start(int numberOfCards, int cardsInGroup) {
        MemoryGame memoryGame = MemoryGameFactory.create(numberOfCards, cardsInGroup);

        MemoryGameState gameState = memoryGame.state();
        memoryGameRepository.save(gameState);

        return gameState;
    }

    public GuessResult turnCard(MemoryGameId memoryGameId, FlatItemId flatItemId) {
        MemoryGameState gameState = memoryGameRepository.load(memoryGameId);
        MemoryGame game = MemoryGameFactory.restore(gameState);

        GuessResult guessResult = game.turnCard(flatItemId);

        memoryGameRepository.save(guessResult.state());

        return guessResult;
    }

    public MemoryGameState getState(MemoryGameId memoryGameId) {
        return memoryGameRepository.load(memoryGameId);
    }

}
