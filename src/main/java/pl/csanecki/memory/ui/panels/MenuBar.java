package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.dialogs.AboutDialog;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    private static final String MAIN_MENU = "Plik";
    private static final String MAIN_MENU_ABOUT_ITEM = "O programie";
    private static final String MAIN_MENU_EXIT_ITEM = "Zamknij";

    private MenuBar() {
    }

    public static MenuBar create(JFrame owner) {
        MenuBar menuBar = new MenuBar();
        menuBar.add(prepareMainMenu(owner));
        return menuBar;
    }

    private static JMenu prepareMainMenu(JFrame owner) {
        JMenu main = new JMenu(MAIN_MENU);

        JMenuItem aboutItem = new JMenuItem(MAIN_MENU_ABOUT_ITEM);
        aboutItem.addActionListener(event -> new AboutDialog(owner));
        main.add(aboutItem);

        main.addSeparator();

        JMenuItem exitItem = new JMenuItem(MAIN_MENU_EXIT_ITEM);
        exitItem.addActionListener(event -> System.exit(0));
        main.add(exitItem);

        return main;
    }
}
