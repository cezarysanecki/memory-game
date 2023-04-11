package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.UiConfig;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel(UiConfig uiConfig) {
        ScoreLabel scoreLabel = ScoreLabel.render();
        CardsPanel cardsPanel = CardsPanel.render(uiConfig);

        setLayout(null);

        add(scoreLabel);
        add(cardsPanel);

        int width1 = cardsPanel.getWidth();
        scoreLabel.setBounds(new Rectangle(0, 0, width1, 40));
        cardsPanel.setBounds(0, scoreLabel.getHeight(), cardsPanel.getWidth(), cardsPanel.getHeight());

        int width = Math.max(cardsPanel.getWidth(), scoreLabel.getWidth());
        int height = scoreLabel.getHeight() + cardsPanel.getHeight();

        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);

    }
}