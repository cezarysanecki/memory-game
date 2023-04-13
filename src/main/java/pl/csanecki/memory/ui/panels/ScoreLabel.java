package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.util.MillisRefreshment;
import pl.csanecki.memory.util.MillisTimerSubscriber;

import javax.swing.*;
import java.time.Duration;

import static pl.csanecki.memory.ui.UiConfig.*;

public class ScoreLabel extends JLabel implements MillisTimerSubscriber {

    private static final String WELCOME_TEXT = "Let's start the game!";

    private ScoreLabel() {
        super(WELCOME_TEXT);
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        setBackground(SCORE_LABEL_BACKGROUND_COLOR);
        setForeground(SCORE_LABEL_FOREGROUND_COLOR);
        setFont(GLOBAL_FONT);
    }

    public static ScoreLabel render() {
        return new ScoreLabel();
    }

    public void reset() {
        setText(WELCOME_TEXT);
    }

    @Override
    public void update(Duration passed, MillisRefreshment millisRefreshment) {
        String scoreText = switch (millisRefreshment) {
            case OneThousand -> String.format("Timer: %ds", passed.toSeconds());
            case OneHundred -> String.format("Timer: %d.%ds", passed.toSeconds(), passed.toMillisPart() / 100);
            case Ten -> String.format("Timer: %d.%02ds", passed.toSeconds(), passed.toMillisPart() / 10);
        };
        setText(scoreText);
    }
}
