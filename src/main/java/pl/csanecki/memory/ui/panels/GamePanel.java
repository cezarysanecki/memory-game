package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.EngineGameConfig;
import pl.csanecki.memory.engine.GuessResult;
import pl.csanecki.memory.engine.MemoryGame;
import pl.csanecki.memory.ui.items.GraphicCard;
import pl.csanecki.memory.ui.items.GraphicCards;
import pl.csanecki.memory.util.MillisTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class GamePanel extends JPanel {

    private final GraphicCards graphicCards;

    private final MillisTimer millisTimer;

    private final MemoryGame memoryGame;

    private boolean started = false;

    public GamePanel(EngineGameConfig gameConfig) {
        millisTimer = MillisTimer.ofOneThousandMilliseconds();
        memoryGame = MemoryGame.create(gameConfig.countNumbersOfCards(), gameConfig.numberOfCardsInGroup);
        graphicCards = GraphicCards.createShuffled(memoryGame.currentState(), gameConfig.reverseImage, gameConfig.obverseImages);

        setLayout(null);

        ScoreLabel scoreLabel = new ScoreLabel(gameConfig.columns * 110 + 10);
        millisTimer.registerSubscribers(Set.of(scoreLabel));
        add(scoreLabel);


        int sizeCards = graphicCards.size();

        for (int row = 0; row < gameConfig.rows; row++)
            for (int column = 0; column < gameConfig.columns; column++) {
                sizeCards--;
                GraphicCard graphicCard = graphicCards.get(sizeCards);
                graphicCard.setBounds(column * 110 + 10, row * 110 + scoreLabel.getHeight(), 100, 100);
                add(graphicCard, BorderLayout.CENTER);
            }

        int widthGamePanel = gameConfig.columns * 110 + 10;
        int heightGamePanel = gameConfig.rows * 110 + scoreLabel.getHeight();

        Dimension dimension = new Dimension(widthGamePanel, heightGamePanel);
        setPreferredSize(dimension);
        setMinimumSize(dimension);


        addMouseListener(new ClickMouseListener());
    }

    private class ClickMouseListener extends MouseAdapter {

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