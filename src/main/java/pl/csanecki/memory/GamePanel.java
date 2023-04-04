package pl.csanecki.memory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

public class GamePanel extends JPanel {

    private int rows = 5;
    private int columns = 8;
    private int widthGamePanel = columns * 110 + 10;
    private int heightGamePanel = rows * 110 + 40;

    private ArrayList<GraphicCard> graphicCards = new ArrayList<>();
    private GraphicCard current;
    private GraphicCard firstGraphicCardPair;
    private GraphicCard secondGraphicCardPair;

    private int guessed = 0;
    private int seconds;
    private Timer timer;

    private int number = 1;
    private int numberOfCards = 40;

    public GamePanel() {
        setLayout(null);

        // dodanie paska wyniku
        Score labelScore = new Score(seconds, columns * 110 + 10);
        timer = new Timer(100, e -> {
            seconds += 1;
            labelScore.setText("Time Score: " + (double) (seconds) / 10 + " s");
        });
        add(labelScore);

        addMouseListener(new MouseClick());

        for (int i = 0; i < numberOfCards; i++) {
            GraphicCard graphicCard = new GraphicCard("" + number, "/img/ziemia.png", "/img/" + number + ".png");
            graphicCards.add(graphicCard);
            add(graphicCard, BorderLayout.CENTER);

            if (i % 2 == 1) {
                number++;
            }
        }

        int sizeCards = graphicCards.size();
        Collections.shuffle(graphicCards);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++) {
                sizeCards--;
                GraphicCard graphicCard = graphicCards.get(sizeCards);
                graphicCard.setBounds(c * 110 + 10, r * 110 + labelScore.getHeight(), 100, 100);
                add(graphicCard, BorderLayout.CENTER);
            }

        setPreferredSize(new Dimension(widthGamePanel, heightGamePanel));
        setMinimumSize(new Dimension(widthGamePanel, heightGamePanel));
    }

    private class MouseClick extends MouseAdapter {

        public GraphicCard find(Point2D p) {
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

        @Override
        public void mousePressed(MouseEvent e) {
            if (guessed == 0) {
                timer.start();
            }
            current = find(e.getPoint());

            if (current != null && current.isNotGuessed()) {
                if (current.isNotGuessed() && firstGraphicCardPair == null) {
                    firstGraphicCardPair = current;
                    firstGraphicCardPair.turnCard();
                } else if (current.isNotGuessed() && secondGraphicCardPair == null && firstGraphicCardPair != current) {
                    secondGraphicCardPair = current;
                    secondGraphicCardPair.turnCard();
                    if (firstGraphicCardPair.equals(secondGraphicCardPair)) {
                        firstGraphicCardPair.markAsGuessed();
                        secondGraphicCardPair.markAsGuessed();
                        guessed++;
                        if (guessed == 20) timer.stop();
                        resetSelectedCards();
                    }
                } else if (firstGraphicCardPair != null && secondGraphicCardPair != null) {
                    if (firstGraphicCardPair.isNotGuessed() && secondGraphicCardPair.isNotGuessed()) {
                        firstGraphicCardPair.turnCard();
                        secondGraphicCardPair.turnCard();
                    }
                    resetSelectedCards();
                }
            } else if (firstGraphicCardPair != null && secondGraphicCardPair != null) {
                if (firstGraphicCardPair.isNotGuessed() && secondGraphicCardPair.isNotGuessed()) {
                    firstGraphicCardPair.turnCard();
                    secondGraphicCardPair.turnCard();
                }
                resetSelectedCards();
            }
        }
    }

    private void resetSelectedCards() {
        firstGraphicCardPair = null;
        secondGraphicCardPair = null;
    }
}