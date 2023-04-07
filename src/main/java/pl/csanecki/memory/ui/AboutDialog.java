package pl.csanecki.memory.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

class AboutDialog extends GenericModalDialog {

    private static final String TITLE = "O programie";
    private static final String FONT_SERIF_NAME = "Serif";
    private static final String R_SYMBOL = "®";
    private static final String AUTHOR = "Cezary Sanecki";
    private static final String GAME_TITLE = "Memory: Animals";
    private static final String ACCEPT_BUTTON_NAME = "OK";

    AboutDialog(JFrame owner) {
        super(owner, TITLE);
        setLayout(new GridLayout(4, 1));

        add(gamePanel());
        add(new LineComponent());
        add(authorPanel());
        add(buttonPanel());

        setVisible(true);
    }

    private JPanel gamePanel() {
        JPanel gamePanel = new JPanel();
        Font font = new Font(FONT_SERIF_NAME, Font.BOLD, 25);
        JLabel text = new JLabel(GAME_TITLE);
        text.setFont(font);
        gamePanel.add(text, BorderLayout.SOUTH);
        return gamePanel;
    }

    private JPanel authorPanel() {
        JPanel authorPanel = new JPanel();
        Font font = new Font(FONT_SERIF_NAME, Font.ITALIC, 18);
        JLabel text = new JLabel(R_SYMBOL + " " + AUTHOR);
        text.setFont(font);
        authorPanel.add(text, BorderLayout.SOUTH);
        return authorPanel;
    }

    private JPanel buttonPanel() {
        JButton okButton = new JButton(ACCEPT_BUTTON_NAME);
        okButton.addActionListener(event -> setVisible(false));

        JPanel helperPanel = new JPanel();
        helperPanel.add(okButton);
        return helperPanel;
    }

    private static class LineComponent extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Line2D line = new Line2D.Double(0, 0, getWidth(), 0);
            g2.draw(line);
        }
    }
}
