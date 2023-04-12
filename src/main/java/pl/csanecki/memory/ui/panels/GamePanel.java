package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.UiConfig;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private static final int HEIGHT_OF_SCORE_PANEL = 40;

    public GamePanel(UiConfig uiConfig) {
        ScoreLabel scoreLabel = ScoreLabel.render();
        CardsPanel cardsPanel = CardsPanel.render(uiConfig);

        setLayout(null);

        add(scoreLabel);
        add(cardsPanel);

        int calculatedCardsPanelWidth = cardsPanel.getWidth();
        scoreLabel.setBounds(new Rectangle(0, 0, calculatedCardsPanelWidth, HEIGHT_OF_SCORE_PANEL));
        cardsPanel.setBounds(0, scoreLabel.getHeight(), cardsPanel.getWidth(), cardsPanel.getHeight());

        int width = Math.max(cardsPanel.getWidth(), scoreLabel.getWidth());
        int height = scoreLabel.getHeight() + cardsPanel.getHeight();

        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);
    }
}