package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.dialogs.AboutDialog;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

public class MenuBar extends JMenuBar {

    private static final String MAIN_MENU = "Plik";
    private static final String MAIN_MENU_RESET_ITEM = "Od nowa";
    private static final String MAIN_MENU_ABOUT_ITEM = "O programie";
    private static final String MAIN_MENU_EXIT_ITEM = "Zamknij";

    private final Collection<MenuBarSubscriber> subscribers = new ArrayList<>();

    public MenuBar(JFrame owner) {
        JMenu main = new JMenu(MAIN_MENU);

        JMenuItem resetItem = createMenuItem(MAIN_MENU_RESET_ITEM, event -> subscribers.forEach(subscriber -> subscriber.update(MenuOption.Reset)));
        JMenuItem aboutItem = createMenuItem(MAIN_MENU_ABOUT_ITEM, event -> new AboutDialog(owner));
        JMenuItem exitItem = createMenuItem(MAIN_MENU_EXIT_ITEM, event -> System.exit(0));

        main.add(resetItem);

        main.addSeparator();

        main.add(aboutItem);
        main.add(exitItem);

        add(main);
    }

    public void register(MenuBarSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    private static JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        return menuItem;
    }
}
