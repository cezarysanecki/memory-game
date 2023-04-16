package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.config.*;
import pl.csanecki.memory.ui.dialogs.AboutDialog;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

public class MenuBar extends JMenuBar implements GamePanelSubscriber {

    private final Collection<MenuBarSubscriber> subscribers = new ArrayList<>();
    private final JMenuItem resetItem;
    private final JMenu options;

    public MenuBar(JFrame owner, CustomConfig customConfig) {
        JMenu main = new JMenu("Plik");

        this.resetItem = createMenuItem("Od nowa", event -> subscribers.forEach(subscriber -> subscriber.update(MenuOption.Reset)));
        this.resetItem.setEnabled(false);
        AboutDialog aboutDialog = new AboutDialog(owner);
        JMenuItem aboutItem = createMenuItem("O programie", event -> aboutDialog.setVisible(true));
        JMenuItem exitItem = createMenuItem("Zamknij", event -> System.exit(0));

        main.add(resetItem);
        main.addSeparator();
        main.add(aboutItem);
        main.add(exitItem);

        this.options = new JMenu("Opcje");
        JMenu gameSizeMenu = new JMenu("Rozmiar planszy");

        JMenuItem gameSizeMenuSmall = createMenuItem("Mały",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeGameSize(GameSize.Small);
                    subscriber.update(customConfig);
                }));
        JMenuItem gameSizeMenuMedium = createMenuItem("Średni",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeGameSize(GameSize.Medium);
                    subscriber.update(customConfig);
                }));
        JMenuItem gameSizeMenuLarge = createMenuItem("Duży",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeGameSize(GameSize.Large);
                    subscriber.update(customConfig);
                }));

        gameSizeMenu.add(gameSizeMenuSmall);
        gameSizeMenu.add(gameSizeMenuMedium);
        gameSizeMenu.add(gameSizeMenuLarge);

        JMenu reverseThemeMenu = new JMenu("Motyw rewersu");

        JMenuItem reverseThemeMenuEarth = createMenuItem("Ziemia",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeReverseTheme(ReverseTheme.Earth);
                    subscriber.update(customConfig);
                }));
        JMenuItem reverseThemeMenuJungle = createMenuItem("Dżungla",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeReverseTheme(ReverseTheme.Jungle);
                    subscriber.update(customConfig);
                }));
        JMenuItem reverseThemeMenuPremierLeague = createMenuItem("Premier League",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeReverseTheme(ReverseTheme.PremierLeague);
                    subscriber.update(customConfig);
                }));

        reverseThemeMenu.add(reverseThemeMenuEarth);
        reverseThemeMenu.add(reverseThemeMenuJungle);
        reverseThemeMenu.add(reverseThemeMenuPremierLeague);

        JMenu obversesThemeMenu = new JMenu("Motyw awersu");

        JMenuItem obversesThemeMenuEnglishClubs = createMenuItem("Kluby angielskie",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeObversesTheme(ObversesTheme.EnglishClubs);
                    subscriber.update(customConfig);
                }));
        JMenuItem obversesThemeMenuAnimals = createMenuItem("Zwierzęta",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeObversesTheme(ObversesTheme.Animals);
                    subscriber.update(customConfig);
                }));

        obversesThemeMenu.add(obversesThemeMenuEnglishClubs);
        obversesThemeMenu.add(obversesThemeMenuAnimals);

        JMenu numberOfCardsInGroupMenu = new JMenu("Ilość kart w grupie");

        JMenuItem numberOfCardsInGroupMenuTwo = createMenuItem("Dwie",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Two);
                    subscriber.update(customConfig);
                }));
        JMenuItem numberOfCardsInGroupMenuThree = createMenuItem("Trzy",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Three);
                    subscriber.update(customConfig);
                }));
        JMenuItem numberOfCardsInGroupMenuFour = createMenuItem("Cztery",
                event -> subscribers.forEach(subscriber -> {
                    customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Four);
                    subscriber.update(customConfig);
                }));

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
        resetItem.setEnabled(gameState != CurrentGameState.Idle);
    }
}
