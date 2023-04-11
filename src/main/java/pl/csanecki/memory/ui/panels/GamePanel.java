package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.UiConfig;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel(UiConfig uiConfig) {
        ScoreLabel scoreLabel = ScoreLabel.render();
        CardsPanel cardsPanel = CardsPanel.render(uiConfig);

        setLayout(null);

        cardsPanel.getWidth();

        int widthGamePanel = uiConfig.columns * 110 + 10;
        int heightGamePanel = uiConfig.rows * 110 + scoreLabel.getHeight();

        Dimension dimension = new Dimension(widthGamePanel, heightGamePanel);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }
}