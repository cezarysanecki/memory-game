package pl.csanecki.memory;

import javax.swing.*;

public class ScoreLabel extends JLabel {

    public ScoreLabel(double seconds, int width) {
        super("Time Score: " + seconds + " s");
        setHorizontalAlignment(SwingConstants.CENTER);
        setBounds(0, 0, width, 40);
    }

    public void updateSeconds(int seconds) {
        setText("Time Score: " + (double) (seconds) / 10 + " s");
    }
}
