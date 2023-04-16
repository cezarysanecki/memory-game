package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.UiConfig;
import pl.csanecki.memory.ui.menu.MainOptions;
import pl.csanecki.memory.ui.menu.MainOptionsMenuSubscriber;
import pl.csanecki.memory.util.MillisTimer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class GamePanel extends JPanel implements CardsPanelSubscriber, MainOptionsMenuSubscriber {

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

        setSizes();

        scoreLabel.setLocation(0, 0);
        cardsPanel.setLocation(0, scoreLabel.getHeight());
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

    public void adjustTo(UiConfig uiConfig) {
        cardsPanel.adjustTo(uiConfig);
        setSizes();
    }

    @Override
    public void update(MainOptions mainOptions) {
        if (mainOptions == MainOptions.Reset) {
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

    private void setSizes() {
        Dimension dimension = resolveDimension();
        setPreferredSize(dimension);

        scoreLabel.setSize(new Dimension((int) dimension.getWidth(), HEIGHT_OF_SCORE_PANEL));
    }

    private Dimension resolveDimension() {
        int width = cardsPanel.getWidth();
        int height = scoreLabel.getHeight() + cardsPanel.getHeight();
        return new Dimension(width, height);
    }
}