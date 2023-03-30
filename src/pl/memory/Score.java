package pl.memory;

import javax.swing.*;

public class Score extends JLabel {

    public Score(double seconds, int width) {
        super("Time Score: " + seconds + " s");
        setHorizontalAlignment(SwingConstants.CENTER);
        setBounds(0, 0, width, 40);
    }
}
