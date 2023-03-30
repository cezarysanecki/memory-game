package pl.memory;

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

    private ArrayList<Card> cards = new ArrayList<>();
    private Card current;
    private Card firstCardPair;
    private Card secondCardPair;

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
            Card card = new Card("" + number, "img/ziemia.png", "img/" + number + ".png");
            cards.add(card);
            add(card, BorderLayout.CENTER);

            if (i % 2 == 1) {
                number++;
            }
        }

        int sizeCards = cards.size();
        Collections.shuffle(cards);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++) {
                sizeCards--;
                Card card = cards.get(sizeCards);
                card.setBounds(c * 110 + 10, r * 110 + labelScore.getHeight(), 100, 100);
                add(card, BorderLayout.CENTER);
            }

        setPreferredSize(new Dimension(widthGamePanel, heightGamePanel));
        setMinimumSize(new Dimension(widthGamePanel, heightGamePanel));
    }

    private class MouseClick extends MouseAdapter {

        public Card find(Point2D p) {
            for (Card card : cards) {
                Rectangle rectangle = new Rectangle(
                    card.getLocation().x, card.getLocation().y,
                    card.getWidth(), card.getHeight());

                if (rectangle.contains(p)) {
                    return card;
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
                if (current.isNotGuessed() && firstCardPair == null) {
                    firstCardPair = current;
                    firstCardPair.turnCard();
                } else if (current.isNotGuessed() && secondCardPair == null && firstCardPair != current) {
                    secondCardPair = current;
                    secondCardPair.turnCard();
                    if (firstCardPair.equals(secondCardPair)) {
                        firstCardPair.markAsGuessed();
                        secondCardPair.markAsGuessed();
                        guessed++;
                        if (guessed == 20) timer.stop();
                        resetSelectedCards();
                    }
                } else if (firstCardPair != null && secondCardPair != null) {
                    if (firstCardPair.isNotGuessed() && secondCardPair.isNotGuessed()) {
                        firstCardPair.turnCard();
                        secondCardPair.turnCard();
                    }
                    resetSelectedCards();
                }
            } else if (firstCardPair != null && secondCardPair != null) {
                if (firstCardPair.isNotGuessed() && secondCardPair.isNotGuessed()) {
                    firstCardPair.turnCard();
                    secondCardPair.turnCard();
                }
                resetSelectedCards();
            }
        }
    }

    private void resetSelectedCards() {
        firstCardPair = null;
        secondCardPair = null;
    }
}