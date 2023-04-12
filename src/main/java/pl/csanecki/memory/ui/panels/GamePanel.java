package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.UiConfig;
import pl.csanecki.memory.util.MillisTimer;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements CardsPanelSubscriber, MenuBarSubscriber {

    private static final int HEIGHT_OF_SCORE_PANEL = 40;

    private final MillisTimer millisTimer = MillisTimer.ofTenMilliseconds();
    private final ScoreLabel scoreLabel;
    private final CardsPanel cardsPanel;
    private boolean underway = false;

    public GamePanel(UiConfig uiConfig) {
        scoreLabel = ScoreLabel.render();
        cardsPanel = CardsPanel.render(uiConfig);

        millisTimer.registerSubscriber(scoreLabel);
        cardsPanel.registerSubscriber(this);

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

    @Override
    public void update(boolean finished) {
        if (!underway) {
            underway = true;
            millisTimer.start();
        }
        if (finished) {
            millisTimer.stop();
        }
    }

    @Override
    public void update(MenuOption menuOption) {
        if (menuOption == MenuOption.Reset) {
            underway = false;
            scoreLabel.reset();
            cardsPanel.reset();
        }
    }
}