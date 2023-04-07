package pl.csanecki.memory.ui;

import pl.csanecki.memory.GameConfig;
import pl.csanecki.memory.GamePanel;

import javax.swing.*;
import java.awt.*;

public class MemoryGameBoard extends JFrame {

    private static final int WIDTH_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int HEIGHT_SCREEN = Toolkit.getDefaultToolkit().getScreenSize().height;

    private static final String MAIN_MENU = "Plik";
    private static final String MAIN_MENU_ABOUT_ITEM = "O programie";
    private static final String MAIN_MENU_EXIT_ITEM = "Zamknij";

    public MemoryGameBoard(GameConfig gameConfig) {
        JMenuBar menuBar = prepareMenuBar();
        setJMenuBar(menuBar);

        add(new GamePanel(gameConfig), BorderLayout.CENTER);

        pack();
        setBounds(
                (WIDTH_SCREEN - getWidth()) / 2, (HEIGHT_SCREEN - getHeight()) / 2,
                getWidth(), getHeight());
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar prepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu main = prepareMainMenu();
        menuBar.add(main);

        return menuBar;
    }

    private JMenu prepareMainMenu() {
        JMenu main = new JMenu(MAIN_MENU);

        JMenuItem aboutItem = new JMenuItem(MAIN_MENU_ABOUT_ITEM);
        aboutItem.addActionListener(event -> new AboutDialog(this));
        main.add(aboutItem);

        main.addSeparator();

        JMenuItem exitItem = new JMenuItem(MAIN_MENU_EXIT_ITEM);
        exitItem.addActionListener(event -> System.exit(0));
        main.add(exitItem);

        return main;
    }
}
