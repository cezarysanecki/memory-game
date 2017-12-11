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
    private ArrayList<Card> cards = new ArrayList<>();
    private Card current;
    private int widthGamePanel;
    private int heightGamePanel;
    private Card firstCardPair;
    private Card secondCardPair;
    private int number = 1;
    private int guessed = 0;
    private int seconds;
    private Timer timer;
    private JLabel timerLabel;

    public GamePanel() {
        setLayout(null);
        timer = new Timer(100, e -> {
            seconds += 1;
            System.out.println(seconds);
            timerLabel.setText("Time Score: " + (double)(seconds) / 10 + " s");
        });
        timerLabel = new JLabel("Time Score: " + seconds + " s");
        timerLabel.setBounds(0,0, columns * 110 + 10, 40);
        add(timerLabel);
        addMouseListener(new MouseClick());

        widthGamePanel = columns * 110 + 10;
        heightGamePanel = rows * 110 + 60;

        for (int i = 0; i < 40; i++) {
            Card card = new Card("" + number, "ziemia.png", "" + number + ".png");
            cards.add(card);
            add(card, BorderLayout.CENTER);

            if (i % 2 == 1) number++;
        }

        System.out.println(cards);
        int sizeCards = cards.size();

        Collections.shuffle(cards);
        System.out.println(cards);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++) {
                sizeCards--;
                Card card = cards.get(sizeCards);
                card.setBounds(c * 110 + 10, r * 110 + 50, 100, 100);
                add(card, BorderLayout.CENTER);
            }

        setPreferredSize(new Dimension(widthGamePanel, heightGamePanel));
    }

    private class MouseClick extends MouseAdapter {
        public Card find(Point2D p) {
            for (Card c : cards) {
                if (new Rectangle(c.getLocation().x, c.getLocation().y, c.getWidth(), c.getHeight()).contains(p))
                    return c;
            }
            return null;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (guessed == 0) timer.start();
            current = find(e.getPoint());
            if (current != null && current.getGuessed() == false ) {
                if (current.getGuessed() == false && firstCardPair == null) {
                    firstCardPair = current;
                    firstCardPair.turnCard();
                } else if (current.getGuessed() == false && secondCardPair == null && firstCardPair != current) {
                    secondCardPair = current;
                    secondCardPair.turnCard();
                    if (firstCardPair.equals(secondCardPair)) {
                        firstCardPair.setGuessed(true);
                        secondCardPair.setGuessed(true);
                        guessed++;
                        if (guessed == 20) timer.stop();
                        firstCardPair = null;
                        secondCardPair = null;
                    }
                } else if (firstCardPair != null && secondCardPair != null){
                    if(firstCardPair.getGuessed() == false && secondCardPair.getGuessed() == false) {
                        firstCardPair.turnCard();
                        secondCardPair.turnCard();
                    }
                    firstCardPair = null;
                    secondCardPair = null;
                }
            } else if (firstCardPair != null && secondCardPair != null) {
                if(firstCardPair.getGuessed() == false && secondCardPair.getGuessed() == false) {
                    firstCardPair.turnCard();
                    secondCardPair.turnCard();
                }
                firstCardPair = null;
                secondCardPair = null;
            }
        }
    }
}