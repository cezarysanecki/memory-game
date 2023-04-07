package pl.csanecki.memory;

import pl.csanecki.memory.util.Subscriber;

import javax.swing.*;
import java.time.Duration;

public class ScoreLabel extends JLabel implements Subscriber {

    public ScoreLabel(int width) {
        super("Time Score: " + 0 + " s");
        setHorizontalAlignment(SwingConstants.CENTER);
        setBounds(0, 0, width, 40);
    }

    public void updateSeconds(int seconds) {
        setText("Time Score: " + (double) (seconds) / 10 + " s");
    }

    @Override
    public void update(Duration passed) {
        updateSeconds(passed.toSecondsPart());
    }
}
