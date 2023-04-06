package pl.csanecki.memory;

import javax.swing.*;
import java.awt.*;

public class MemoryFrame extends JFrame {

    private static final String MAIN_MENU = "Plik";
    private static final String MAIN_MENU_ABOUT_ITEM = "O programie";
    private static final String MAIN_MENU_EXIT_ITEM = "Zamknij";

    public static void main(String[] args) {
        new MemoryFrame();
    }

    public MemoryFrame() {
        JMenuBar menuBar = prepareMenuBar();
        setJMenuBar(menuBar);

        add(new GamePanel(), BorderLayout.CENTER);

        pack();
        int widthScreen = Toolkit.getDefaultToolkit().getScreenSize().width;
        int heightScreen = Toolkit.getDefaultToolkit().getScreenSize().height;
        setBounds(
                (widthScreen - getWidth()) / 2,
                (heightScreen - getHeight()) / 2,
                getWidth(),
                getHeight());
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
