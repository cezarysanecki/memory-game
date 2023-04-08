package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.Divider;
import pl.csanecki.memory.EngineGameConfig;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.engine.GuessResult;
import pl.csanecki.memory.engine.MemoryGame;
import pl.csanecki.memory.ui.items.GraphicCard;
import pl.csanecki.memory.ui.items.GraphicCards;
import pl.csanecki.memory.util.MillisTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel {

    private final GraphicCards graphicCards;

    private final MillisTimer millisTimer;

    private final MemoryGame memoryGame;

    private boolean started = false;

    public GamePanel(EngineGameConfig gameConfig) {
        Divider divider = Divider.create(gameConfig);

        memoryGame = new MemoryGame(divider.toMemoryGameSetup());
        graphicCards = divider.toGraphicCards();

        setLayout(null);

        ScoreLabel labelScoreLabel = new ScoreLabel(gameConfig.columns * 110 + 10);
        millisTimer = MillisTimer.ofOneThousandMilliseconds();
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

        Dimension dimension = new Dimension(widthGamePanel, heightGamePanel);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }

    private record A(FlatItemsGroupId flatItemsGroupId, ImageIcon obverseImage) {
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