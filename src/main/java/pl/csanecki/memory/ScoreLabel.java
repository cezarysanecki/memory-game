package pl.csanecki.memory;

import pl.csanecki.memory.util.MillisTimerSubscriber;

import javax.swing.*;
import java.time.Duration;

public class ScoreLabel extends JLabel implements MillisTimerSubscriber {

    public ScoreLabel(int width) {
        super("Let's start the game!");
        setHorizontalAlignment(SwingConstants.CENTER);
        setBounds(0, 0, width, 40);
    }

    @Override
    public void update(Duration passed) {
        setText(String.format("Timer: %d.%03ds", passed.toSecondsPart(), passed.toMillisPart()));
    }
}
