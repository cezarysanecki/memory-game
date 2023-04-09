package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.util.MillisRefreshment;
import pl.csanecki.memory.util.MillisTimerSubscriber;

import javax.swing.*;
import java.time.Duration;

public class ScoreLabel extends JLabel implements MillisTimerSubscriber {

    private static final String WELCOME_TEXT = "Let's start the game!";

    public ScoreLabel(int width) {
        super(WELCOME_TEXT);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBounds(0, 0, width, 40);
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
