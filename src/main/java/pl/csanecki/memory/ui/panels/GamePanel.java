package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.UiConfig;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel(UiConfig uiConfig) {
        ScoreLabel scoreLabel = ScoreLabel.render();
        CardsPanel cardsPanel = CardsPanel.render(uiConfig);

        setLayout(null);

        int width = Math.max(cardsPanel.getWidth(), scoreLabel.getWidth());
        int height = scoreLabel.getHeight() + cardsPanel.getHeight();

        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }
}