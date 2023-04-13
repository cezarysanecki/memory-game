package pl.csanecki.memory.ui;

import pl.csanecki.memory.config.CustomConfig;
import pl.csanecki.memory.ui.panels.GamePanel;
import pl.csanecki.memory.ui.panels.MenuBar;

import javax.swing.*;
import java.awt.*;

public class MemoryGameFrame extends JFrame {

    private static final int WIDTH_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int HEIGHT_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().height;

    public MemoryGameFrame(CustomConfig customConfig) {
        MenuBar menubar = new MenuBar(this, customConfig);
        UiConfig uiConfig = UiConfig.create(customConfig);
        GamePanel gamePanel = new GamePanel(uiConfig);

        setJMenuBar(menubar);
        add(gamePanel, BorderLayout.CENTER);

        menubar.registerSubscriber(gamePanel);
        gamePanel.registerSubscriber(menubar);

        pack();
        setLocation((WIDTH_SCREEN - getWidth()) / 2, (HEIGHT_SCREEN - getHeight()) / 2);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
