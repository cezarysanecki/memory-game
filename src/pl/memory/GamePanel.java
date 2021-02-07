package pl.memory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

//karty do gry muszą mieć wymiar 100x100!!! plansza nie jest skalowalna!

public class GamePanel extends JPanel {
    // dane do wymiarów planszy
    private int rows = 5;
    private int columns = 8;
    private int widthGamePanel = columns * 110 + 10;
    private int heightGamePanel = rows * 110 + 40;

    //dane dotyczące mechaniki gry
    private ArrayList<Card> cards = new ArrayList<>();
    private Card current;
    private Card firstCardPair;
    private Card secondCardPair;

    //słuzy do liczenia wyniku
    private int guessed = 0;
    private int seconds;
    private Timer timer;

    // ile będzie kart na planszy
    private int number = 1;
    private int numberOfCards = 40;     //liczone podwójnie

    public GamePanel() {
        setLayout(null);    //layout jest ustawiany ręcznie

        // dodanie paska wyniku
        Score labelScore = new Score(seconds, columns * 110 + 10);
        timer = new Timer(100, e -> {
            seconds += 1;
            labelScore.setText("Time Score: " + (double)(seconds) / 10 + " s");
        });
        add(labelScore);

        // dodanie nasłuchiwacza do obsługi gry
        addMouseListener(new MouseClick());

        //dodawanie kart do gry
        for (int i = 0; i < numberOfCards; i++) {
            Card card = new Card("" + number, "img/ziemia.png", "img/" + number + ".png");
            cards.add(card);
            add(card, BorderLayout.CENTER);

            if (i % 2 == 1) number++;       //tworzy pary
        }

        int sizeCards = cards.size(); //potrzebne do ustawnienia kart na planszy
        Collections.shuffle(cards);   //tasuje karty do rozgrywki

        //pobiera karty z cards i dobiera ich rozmiary na planszy / dodaje je na planszę
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++) {
                sizeCards--;
                Card card = cards.get(sizeCards);
                card.setBounds(c * 110 + 10, r * 110 + labelScore.getHeight(), 100, 100);   //ustawia granice każdej karty
                add(card, BorderLayout.CENTER);
            }

            //ustawia preferowane rozmiary planszy
        setPreferredSize(new Dimension(widthGamePanel, heightGamePanel));
        setMinimumSize(new Dimension(widthGamePanel, heightGamePanel));
    }

    // klasa wewnetrzna do obługi zdarzeń kliknięcia myszą na karty
    private class MouseClick extends MouseAdapter {

        //metoda do sprawdzenia czy została wybrana karta (czy punkt z event zawiera się w karcie)
        public Card find(Point2D p) {
            for (Card c : cards) {
                if (new Rectangle(c.getLocation().x, c.getLocation().y, c.getWidth(), c.getHeight()).contains(p))
                    return c;
            }
            return null;
        }

        //głowna mechanika gry
        @Override
        public void mousePressed(MouseEvent e) {
            if (guessed == 0) timer.start();        //uruchamia zegar gry przy pierwszym kliknięciu myszą
            current = find(e.getPoint());           //sprawdza czy karta zawiera w sobie punkt z event

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
            }
            else if (firstCardPair != null && secondCardPair != null) {
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