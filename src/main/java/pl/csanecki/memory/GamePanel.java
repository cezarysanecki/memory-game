package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.GuessResult;
import pl.csanecki.memory.engine.MemoryGame;
import pl.csanecki.memory.setup.GameSetupCoordinator;
import pl.csanecki.memory.util.MillisTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GamePanel extends JPanel {

    private final GraphicCards graphicCards;

    private final MillisTimer millisTimer;

    private final MemoryGame memoryGame;

    private boolean started = false;

    public GamePanel(GameConfig gameConfig) {
        GameSetupCoordinator gameSetupCoordinator = gameConfig.createGameSetupCoordinator();
        memoryGame = new MemoryGame(gameSetupCoordinator.toGameSetup());
        graphicCards = gameSetupCoordinator.toGraphicCards();

        setLayout(null);

        ScoreLabel labelScoreLabel = new ScoreLabel(gameConfig.columns * 110 + 10);
        millisTimer = MillisTimer.ofOneHundredMilliseconds();
        millisTimer.registerSubscribers(Set.of(labelScoreLabel));
        add(labelScoreLabel);

        addMouseListener(new MouseClick());

        List<GraphicCard> graphicCardList = graphicCards.getGraphicCards();

        int sizeCards = graphicCardList.size();
        Collections.shuffle(graphicCardList);

        for (int row = 0; row < gameConfig.rows; row++)
            for (int column = 0; column < gameConfig.columns; column++) {
                sizeCards--;
                GraphicCard graphicCard = graphicCardList.get(sizeCards);
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
            if (!started) {
                millisTimer.start();
                started = true;
            }
            Optional<GraphicCard> chosenGraphicCard = graphicCards.findCardByCoordinates(event.getPoint());
            if (chosenGraphicCard.isEmpty()) {
                return;
            }
            FlatItemId flatItemId = chosenGraphicCard.get().getFlatItemId();

            GuessResult result = memoryGame.turnCard(flatItemId);

            switch (result) {
                case Failure -> chosenGraphicCard.get().turnToAverseUp();
                case GameOver -> {
                    millisTimer.stop();
                    graphicCards.refreshAll(memoryGame.currentState());
                }
                default -> graphicCards.refreshAll(memoryGame.currentState());
            }
        }
    }
}