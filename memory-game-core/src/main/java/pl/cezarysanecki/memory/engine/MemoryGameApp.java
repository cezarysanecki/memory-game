package pl.cezarysanecki.memory.engine;

import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.GuessResult;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameView;
import pl.cezarysanecki.memory.engine.db.MemoryGameEntity;
import pl.cezarysanecki.memory.engine.db.MemoryGameRepository;

import java.util.stream.Collectors;

public class MemoryGameApp {

    private final MemoryGameRepository memoryGameRepository;

    public MemoryGameApp(MemoryGameRepository memoryGameRepository) {
        this.memoryGameRepository = memoryGameRepository;
    }

    public static MemoryGameApp inMemory() {
        return new MemoryGameApp(new InMemoryMemoryGameRepository());
    }

    public MemoryGameId start(int numberOfCards, int cardsInGroup) {
        MemoryGameEntity game = MemoryGameFactory.create(numberOfCards, cardsInGroup);

        memoryGameRepository.save(game);

        return game.memoryGameId();
    }

    public GuessResult turnCard(MemoryGameId memoryGameId, FlatItemId flatItemId) {
        MemoryGameEntity gameState = memoryGameRepository.load(memoryGameId);
        MemoryGame game = MemoryGameFactory.restore(gameState);

        GuessResult guessResult = game.turnCard(flatItemId);

        MemoryGameEntity newState = gameState.applyResult(flatItemId, guessResult);
        memoryGameRepository.save(newState);

        return guessResult;
    }

    public MemoryGameView getState(MemoryGameId memoryGameId) {
        MemoryGameEntity entity = memoryGameRepository.load(memoryGameId);
        return new MemoryGameView(
                entity.memoryGameId(),
                entity.flatItems().stream()
                        .map(flatItem -> new MemoryGameView.FlatItem(
                                flatItem.flatItemId(),
                                flatItem.assignedGroupId(),
                                flatItem.obverseUp()
                        ))
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

}
