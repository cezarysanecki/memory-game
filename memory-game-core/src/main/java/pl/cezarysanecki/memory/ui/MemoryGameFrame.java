package pl.cezarysanecki.memory.ui;

import pl.cezarysanecki.memory.config.CustomConfig;
import pl.cezarysanecki.memory.ui.menu.GraphicOptionsMenuSubscriber;
import pl.cezarysanecki.memory.ui.menu.MenuBar;
import pl.cezarysanecki.memory.ui.panels.GamePanel;

import javax.swing.*;
import java.awt.*;

public class MemoryGameFrame extends JFrame implements GraphicOptionsMenuSubscriber {

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

        menubar.registerMainOptionsMenuSubscriber(gamePanel);
        menubar.registerGraphicOptionsMenuSubscriber(this);
        menubar.addGamePanelSubscribers(gamePanel);

        pack();
        setLocation((WIDTH_SCREEN - getWidth()) / 2, (HEIGHT_SCREEN - getHeight()) / 2);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void update(CustomConfig customConfig) {
        UiConfig uiConfig = UiConfig.create(customConfig);
        gamePanel.adjustTo(uiConfig);

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
