package pl.csanecki.memory.ui;

import pl.csanecki.memory.config.CustomConfig;
import pl.csanecki.memory.ui.panels.GamePanel;
import pl.csanecki.memory.ui.panels.MenuBar;
import pl.csanecki.memory.ui.panels.MenuBarSubscriber;
import pl.csanecki.memory.ui.panels.MenuOption;

import javax.swing.*;
import java.awt.*;

public class MemoryGameFrame extends JFrame implements MenuBarSubscriber {

    private static final int WIDTH_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int HEIGHT_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final GamePanel gamePanel;
    private final MenuBar menubar;

    public MemoryGameFrame(CustomConfig customConfig) {
        menubar = new MenuBar(this, customConfig);
        UiConfig uiConfig = UiConfig.create(customConfig);
        gamePanel = new GamePanel(uiConfig);

        setJMenuBar(menubar);
        add(gamePanel, BorderLayout.CENTER);

        menubar.registerSubscriber(gamePanel);
        menubar.registerSubscriber(this);
        gamePanel.registerSubscriber(menubar);

        pack();
        setLocation((WIDTH_SCREEN - getWidth()) / 2, (HEIGHT_SCREEN - getHeight()) / 2);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void update(MenuOption menuOption) {

    }

    @Override
    public void update(CustomConfig customConfig) {
        gamePanel.extracted(customConfig);

        pack();
        setLocation((WIDTH_SCREEN - getWidth()) / 2, (HEIGHT_SCREEN - getHeight()) / 2);
        repaint();
    }

    @Override
    public void repaint() {
        if (menubar != null) {
            menubar.repaint();
        }
        if (gamePanel != null) {
            gamePanel.repaint();
        }
        super.repaint();
        validate();
    }
}
