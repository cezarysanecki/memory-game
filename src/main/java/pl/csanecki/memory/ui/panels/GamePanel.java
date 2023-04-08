package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.EngineGameConfig;
import pl.csanecki.memory.engine.GuessResult;
import pl.csanecki.memory.engine.MemoryGame;
import pl.csanecki.memory.engine.state.FlatItemCurrentState;
import pl.csanecki.memory.engine.state.GroupOfFlatItemsCurrentState;
import pl.csanecki.memory.engine.state.MemoryGameCurrentState;
import pl.csanecki.memory.ui.items.GraphicCard;
import pl.csanecki.memory.ui.items.GraphicCards;
import pl.csanecki.memory.util.MillisTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel {

    private final GraphicCards graphicCards;

    private final MillisTimer millisTimer;

    private final MemoryGame memoryGame;

    private boolean started = false;

    public GamePanel(EngineGameConfig gameConfig) {
        memoryGame = new MemoryGame(gameConfig.columns * gameConfig.rows, gameConfig.numberOfCardsInGroup);

        MemoryGameCurrentState currentState = memoryGame.currentState();
        Set<GroupOfFlatItemsCurrentState> groupOfFlatItemsCurrentStates = currentState.groupOfFlatItems();

        ImageIcon reverseImage = gameConfig.reverseImage;
        List<ImageIcon> obverseImages = gameConfig.obverseImages;

        List<GraphicCard> graphicCards = new ArrayList<>();
        int index = 0;
        for (GroupOfFlatItemsCurrentState groupOfFlatItemsCurrentState : groupOfFlatItemsCurrentStates) {
            ImageIcon obverseImage = obverseImages.get(index);
            for (FlatItemCurrentState flatItemCurrentState : groupOfFlatItemsCurrentState.flatItems()) {
                graphicCards.add(new GraphicCard(flatItemCurrentState.flatItemId(), reverseImage, obverseImage));
            }
            index++;
        }
        Collections.shuffle(graphicCards);

        setLayout(null);

        ScoreLabel labelScoreLabel = new ScoreLabel(gameConfig.columns * 110 + 10);
        millisTimer = MillisTimer.ofOneThousandMilliseconds();
        millisTimer.registerSubscribers(Set.of(labelScoreLabel));
        add(labelScoreLabel);

        addMouseListener(new MouseClick());
        this.graphicCards = new GraphicCards(graphicCards);

        int sizeCards = graphicCards.size();

        for (int row = 0; row < gameConfig.rows; row++)
            for (int column = 0; column < gameConfig.columns; column++) {
                sizeCards--;
                GraphicCard graphicCard = graphicCards.get(sizeCards);
                graphicCard.setBounds(column * 110 + 10, row * 110 + labelScoreLabel.getHeight(), 100, 100);
                add(graphicCard, BorderLayout.CENTER);
            }

        int widthGamePanel = gameConfig.columns * 110 + 10;
        int heightGamePanel = gameConfig.rows * 110 + 40;

        Dimension dimension = new Dimension(widthGamePanel, heightGamePanel);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }

    private class MouseClick extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            if (!started) {
                millisTimer.start();
                started = true;
            }
            graphicCards.findCardByCoordinates(event.getPoint())
                .ifPresent(graphicCard -> {
                    GuessResult result = memoryGame.turnCard(graphicCard.getFlatItemId());
                    switch (result) {
                        case Failure -> graphicCard.turnToObverseUp();
                        case GameOver -> {
                            millisTimer.stop();
                            graphicCards.refreshAll(memoryGame.currentState());
                        }
                        default -> graphicCards.refreshAll(memoryGame.currentState());
                    }
                });
        }
    }
}