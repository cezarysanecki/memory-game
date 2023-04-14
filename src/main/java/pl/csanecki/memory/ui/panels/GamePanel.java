package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.config.CustomConfig;
import pl.csanecki.memory.ui.UiConfig;
import pl.csanecki.memory.util.MillisTimer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class GamePanel extends JPanel implements CardsPanelSubscriber, MenuBarSubscriber {

    private static final int HEIGHT_OF_SCORE_PANEL = 40;

    private final Collection<GamePanelSubscriber> subscribers = new ArrayList<>();
    private final MillisTimer millisTimer = MillisTimer.ofTenMilliseconds();
    private final ScoreLabel scoreLabel;
    private final CardsPanel cardsPanel;
    private boolean underway = false;

    public GamePanel(UiConfig uiConfig) {
        scoreLabel = ScoreLabel.render();
        cardsPanel = new CardsPanel(uiConfig);

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

    public void registerSubscriber(GamePanelSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    @Override
    public void update(CurrentGameState gameState) {
        if (!underway) {
            underway = true;
            millisTimer.start();
        }
        if (gameState != CurrentGameState.Running) {
            millisTimer.stop();
            underway = false;
        }
        subscribers.forEach(subscriber -> subscriber.update(gameState));
    }

    @Override
    public void update(CustomConfig customConfig) {
    }

    public void extracted(CustomConfig customConfig) {
        UiConfig uiConfig = UiConfig.create(customConfig);
        cardsPanel.adjustToConfig(uiConfig);

        int width = cardsPanel.getWidth();
        int height = scoreLabel.getHeight() + cardsPanel.getHeight();

        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);

        scoreLabel.setSize(new Dimension(width, HEIGHT_OF_SCORE_PANEL));
    }

    @Override
    public void update(MenuOption menuOption) {
        if (menuOption == MenuOption.Reset) {
            underway = false;
            millisTimer.stop();
            scoreLabel.reset();
            cardsPanel.reset();
        }
    }

    @Override
    public void repaint() {
        if (cardsPanel != null) {
            cardsPanel.repaint();
        }
        if (scoreLabel != null) {
            scoreLabel.repaint();
        }
        super.repaint();
    }
}