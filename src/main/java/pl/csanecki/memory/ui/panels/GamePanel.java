package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.EngineGameConfig;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel(EngineGameConfig gameConfig) {
        ScoreLabel scoreLabel = ScoreLabel.render();
        CardsPanel cardsPanel = CardsPanel.render(gameConfig);

        setLayout(null);

        cardsPanel.getWidth();

        int widthGamePanel = gameConfig.columns * 110 + 10;
        int heightGamePanel = gameConfig.rows * 110 + scoreLabel.getHeight();

        Dimension dimension = new Dimension(widthGamePanel, heightGamePanel);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }
}