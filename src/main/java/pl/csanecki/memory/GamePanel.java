package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.GroupOfFlatItems;
import pl.csanecki.memory.engine.GuessResult;
import pl.csanecki.memory.engine.MemoryGame;
import pl.csanecki.memory.state.GroupOfFlatItemsCurrentState;
import pl.csanecki.memory.state.MemoryGameCurrentState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GamePanel extends JPanel {

    private final ArrayList<GraphicCard> graphicCards = new ArrayList<>();

    private int seconds;
    private Timer timer;

    private final MemoryGame memoryGame;

    public GamePanel(GameConfig gameConfig) {
        int numberOfElements = GameConfig.countNumberOfElements(gameConfig);
        int numberOfFields = GameConfig.countNumberOfFields(gameConfig);
        if (numberOfElements != numberOfFields) {
            throw new IllegalArgumentException("number of elements and fields must be equal");
        }

        AtomicInteger currentNumber = new AtomicInteger(0);
        Set<GroupOfFlatItems> groupOfFlatItems = gameConfig.groups
                .stream()
                .map(group -> {
                    Set<FlatItemId> flatItemIds = IntStream.of(0, group.numberOfItems)
                            .mapToObj(t -> FlatItemId.of(currentNumber.getAndIncrement()))
                            .collect(Collectors.toUnmodifiableSet());

                    flatItemIds.stream()
                            .map(flatItemId -> new GraphicCard(gameConfig.reverseImage, group.averseImage, flatItemId))
                            .forEach(graphicCards::add);

                    return GroupOfFlatItems.allReversed(flatItemIds);
                })
                .collect(Collectors.toUnmodifiableSet());
        memoryGame = new MemoryGame(groupOfFlatItems);

        setLayout(null);

        ScoreLabel labelScoreLabel = new ScoreLabel(seconds, gameConfig.columns * 110 + 10);
        timer = new Timer(100, event -> {
            seconds += 1;
            labelScoreLabel.updateSeconds(seconds);
        });
        add(labelScoreLabel);

        addMouseListener(new MouseClick());

        int sizeCards = graphicCards.size();
        Collections.shuffle(graphicCards);

        for (int row = 0; row < gameConfig.rows; row++)
            for (int column = 0; column < gameConfig.columns; column++) {
                sizeCards--;
                GraphicCard graphicCard = graphicCards.get(sizeCards);
                graphicCard.setBounds(column * 110 + 10, row * 110 + labelScoreLabel.getHeight(), 100, 100);
                add(graphicCard, BorderLayout.CENTER);
            }

        int widthGamePanel = gameConfig.columns * 110 + 10;
        int heightGamePanel = gameConfig.rows * 110 + 40;

        setPreferredSize(new Dimension(widthGamePanel, heightGamePanel));
        setMinimumSize(new Dimension(widthGamePanel, heightGamePanel));
    }

    private class MouseClick extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            timer.start();
            GraphicCard chosenGraphicCard = findCardByCoordinates(event.getPoint());
            if (chosenGraphicCard == null) {
                return;
            }
            FlatItemId flatItemId = chosenGraphicCard.getFlatItemId();

            GuessResult result = memoryGame.turnCard(flatItemId);
            if (result == GuessResult.GameOver) {
                timer.stop();
            }

            MemoryGameCurrentState currentState = memoryGame.currentState();

            graphicCards.forEach(graphicCard -> currentState.groupOfFlatItems()
                    .stream()
                    .map(GroupOfFlatItemsCurrentState::flatItems)
                    .flatMap(Collection::stream)
                    .filter(flatItem -> flatItem.flatItemId().equals(graphicCard.getFlatItemId()))
                    .findFirst()
                    .ifPresent(graphicCard::refresh));
        }

        private GraphicCard findCardByCoordinates(Point2D p) {
            for (GraphicCard graphicCard : graphicCards) {
                Rectangle rectangle = new Rectangle(
                        graphicCard.getLocation().x, graphicCard.getLocation().y,
                        graphicCard.getWidth(), graphicCard.getHeight());

                if (rectangle.contains(p)) {
                    return graphicCard;
                }
            }
            return null;
        }
    }
}