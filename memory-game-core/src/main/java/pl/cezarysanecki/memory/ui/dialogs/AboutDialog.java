package pl.cezarysanecki.memory.ui.dialogs;

import pl.cezarysanecki.memory.ui.UiConfig;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends GenericModalDialog {

    private static final int WIDTH_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int HEIGHT_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().height;

    private static final String TITLE = "O programie";
    private static final String R_SYMBOL = "®";
    private static final String AUTHOR = "Cezary Sanecki";
    private static final String GAME_TITLE = "Memory: Animals";
    private static final String ACCEPT_BUTTON_NAME = "OK";

    public AboutDialog(JFrame owner) {
        super(owner, TITLE);
        setLayout(new GridLayout(3, 1));

        add(gamePanel());
        add(authorPanel());
        add(buttonPanel());

        pack();
        setLocation((WIDTH_SCREEN - getWidth()) / 2, (HEIGHT_SCREEN - getHeight()) / 2);
    }

    private JPanel gamePanel() {
        JPanel gamePanel = new JPanel();
        JLabel text = new JLabel(GAME_TITLE);
        text.setFont(UiConfig.GLOBAL_FONT);
        gamePanel.add(text, BorderLayout.SOUTH);
        gamePanel.setBackground(UiConfig.CARDS_PANEL_BACKGROUND_COLOR);
        return gamePanel;
    }

    private JPanel authorPanel() {
        JPanel authorPanel = new JPanel();
        JLabel text = new JLabel(R_SYMBOL + " " + AUTHOR);
        text.setFont(UiConfig.GLOBAL_FONT);
        authorPanel.add(text, BorderLayout.SOUTH);
        authorPanel.setBackground(UiConfig.CARDS_PANEL_BACKGROUND_COLOR);
        return authorPanel;
    }

    private JPanel buttonPanel() {
        JButton okButton = new JButton(ACCEPT_BUTTON_NAME);
        okButton.addActionListener(event -> setVisible(false));

        JPanel helperPanel = new JPanel();
        helperPanel.add(okButton);
        helperPanel.setBackground(UiConfig.CARDS_PANEL_BACKGROUND_COLOR);
        return helperPanel;
    }
}
