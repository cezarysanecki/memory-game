package pl.memory;

import javax.swing.*;
import java.awt.*;

public class Card extends JLabel {
    private String name;
    private ImageIcon reverse;
    private ImageIcon observe;
    private ImageIcon activeIcon;
    private boolean guessed = false;

    public Card(String name, String reverse, String observe) {          //Tworzy kartę z rewersem i awersem
        setPreferredSize(new Dimension(100,100));
        this.name = name;
        this.reverse = new ImageIcon(reverse);
        this.observe = new ImageIcon(observe);
        this.activeIcon = this.reverse;
        setIcon(this.activeIcon);
    }

    public void setGuessed(boolean guessed) {
        this.guessed = guessed;
    }   //ustawia kartę czy jest zgadnięta

    public boolean getGuessed() {
        return this.guessed;
    }   //zwraca zgadnięcie karty

    public void turnCard() {                            //odwraca rewers karty na awers i odwrotnie
        if(this.activeIcon == this.reverse)
            this.activeIcon = this.observe;
        else
            this.activeIcon = this.reverse;
        setIcon(this.activeIcon);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(activeIcon.getImage(), 0, 0, null);
    }

    @Override
    public boolean equals(Object obj) {                 //porównanie czy obiekty są równe
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        if (this.name.equals(card.name))
            return true;
        return false;
    }
}
