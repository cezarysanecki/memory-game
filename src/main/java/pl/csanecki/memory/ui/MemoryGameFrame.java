package pl.csanecki.memory.ui;

import pl.csanecki.memory.ui.panels.GamePanel;
import pl.csanecki.memory.ui.panels.MenuBar;

import javax.swing.*;
import java.awt.*;

public class MemoryGameFrame extends JFrame {

    private static final int WIDTH_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int HEIGHT_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().height;

    public MemoryGameFrame(UiConfig uiConfig) {
        MenuBar menubar = MenuBar.create(this);
        GamePanel gamePanel = new GamePanel(uiConfig);

        setJMenuBar(menubar);
        add(gamePanel, BorderLayout.CENTER);

        pack();
        setBounds(
            (WIDTH_SCREEN - getWidth()) / 2, (HEIGHT_SCREEN - getHeight()) / 2,
            getWidth(), getHeight());
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
