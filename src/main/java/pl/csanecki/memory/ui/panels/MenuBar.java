package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.config.CustomConfig;
import pl.csanecki.memory.config.ReverseTheme;
import pl.csanecki.memory.ui.dialogs.AboutDialog;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

public class MenuBar extends JMenuBar implements GamePanelSubscriber {

    private final Collection<MenuBarSubscriber> subscribers = new ArrayList<>();
    private final JMenuItem resetItem;
    private final JMenu options;
    private CustomConfig customConfig;

    public MenuBar(JFrame owner, CustomConfig customConfig) {
        this.customConfig = customConfig;

        JMenu main = new JMenu("Plik");

        this.resetItem = createMenuItem("Od nowa", event -> subscribers.forEach(subscriber -> subscriber.update(MenuOption.Reset)));
        this.resetItem.setEnabled(false);
        JMenuItem aboutItem = createMenuItem("O programie", event -> new AboutDialog(owner));
        JMenuItem exitItem = createMenuItem("Zamknij", event -> System.exit(0));

        main.add(resetItem);
        main.addSeparator();
        main.add(aboutItem);
        main.add(exitItem);

        this.options = new JMenu("Opcje");
        JMenu gameSizeMenu = new JMenu("Rozmiar planszy");

        JMenuItem gameSizeMenuSmall = new JMenuItem("Mały");
        JMenuItem gameSizeMenuMedium = new JMenuItem("Średni");
        JMenuItem gameSizeMenuLarge = new JMenuItem("Duży");

        gameSizeMenu.add(gameSizeMenuSmall);
        gameSizeMenu.add(gameSizeMenuMedium);
        gameSizeMenu.add(gameSizeMenuLarge);

        JMenu reverseThemeMenu = new JMenu("Motyw rewersu");

        JMenuItem reverseThemeMenuEarth = createMenuItem("Ziemia", event -> subscribers.forEach(subscriber -> subscriber.update(customConfig.changeReverseTheme(ReverseTheme.Earth))));
        JMenuItem reverseThemeMenuJungle = new JMenuItem("Dżungla");
        JMenuItem reverseThemeMenuPremierLeague = new JMenuItem("Premier League");

        reverseThemeMenu.add(reverseThemeMenuEarth);
        reverseThemeMenu.add(reverseThemeMenuJungle);
        reverseThemeMenu.add(reverseThemeMenuPremierLeague);

        JMenu obversesThemeMenu = new JMenu("Motyw awersu");

        JMenuItem obversesThemeMenuEnglishClubs = new JMenuItem("Kluby angielskie");
        JMenuItem obversesThemeMenuAnimals = new JMenuItem("Zwierzęta");

        obversesThemeMenu.add(obversesThemeMenuEnglishClubs);
        obversesThemeMenu.add(obversesThemeMenuAnimals);

        JMenu numberOfCardsInGroupMenu = new JMenu("Ilość kart w grupie");

        JMenuItem numberOfCardsInGroupMenuTwo = new JMenuItem("Dwie");
        JMenuItem numberOfCardsInGroupMenuThree = new JMenuItem("Trzy");
        JMenuItem numberOfCardsInGroupMenuFour = new JMenuItem("Cztery");

        numberOfCardsInGroupMenu.add(numberOfCardsInGroupMenuTwo);
        numberOfCardsInGroupMenu.add(numberOfCardsInGroupMenuThree);
        numberOfCardsInGroupMenu.add(numberOfCardsInGroupMenuFour);

        options.add(gameSizeMenu);
        options.add(reverseThemeMenu);
        options.add(obversesThemeMenu);
        options.add(numberOfCardsInGroupMenu);

        add(main);
        add(options);
    }

    public void registerSubscriber(MenuBarSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    private static JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        return menuItem;
    }

    @Override
    public void update(CurrentGameState gameState) {
        options.setEnabled(gameState == CurrentGameState.Idle);
        resetItem.setEnabled(gameState == CurrentGameState.Running);
    }
}
