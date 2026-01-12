package pl.cezarysanecki.memory.ui.panels;

import pl.cezarysanecki.memory.ui.util.MillisRefreshment;
import pl.cezarysanecki.memory.ui.util.MillisTimerSubscriber;

import javax.swing.*;
import java.time.Duration;

import static pl.cezarysanecki.memory.ui.UiConfig.GLOBAL_FONT;
import static pl.cezarysanecki.memory.ui.UiConfig.SCORE_LABEL_BACKGROUND_COLOR;
import static pl.cezarysanecki.memory.ui.UiConfig.SCORE_LABEL_FOREGROUND_COLOR;

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
