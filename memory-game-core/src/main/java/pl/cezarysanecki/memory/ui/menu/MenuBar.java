package pl.cezarysanecki.memory.ui.menu;

import pl.cezarysanecki.memory.config.CustomConfig;
import pl.cezarysanecki.memory.ui.panels.GamePanel;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    private final MainOptionsMenu mainOptionsMenu;
    private final GraphicOptionsMenu graphicOptionsMenu;

    public MenuBar(JFrame owner, CustomConfig customConfig) {
        mainOptionsMenu = new MainOptionsMenu(owner);
        graphicOptionsMenu = new GraphicOptionsMenu(customConfig);

        add(mainOptionsMenu);
        add(graphicOptionsMenu);
    }

    public void registerMainOptionsMenuSubscriber(MainOptionsMenuSubscriber subscriber) {
        this.mainOptionsMenu.registerSubscriber(subscriber);
    }

    public void registerGraphicOptionsMenuSubscriber(GraphicOptionsMenuSubscriber subscriber) {
        this.graphicOptionsMenu.registerSubscriber(subscriber);
    }

    public void addGamePanelSubscribers(GamePanel gamePanel) {
        gamePanel.registerSubscriber(mainOptionsMenu);
        gamePanel.registerSubscriber(graphicOptionsMenu);
    }
}
