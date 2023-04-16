package pl.csanecki.memory.ui.menu;

import pl.csanecki.memory.config.*;
import pl.csanecki.memory.ui.panels.CurrentGameState;
import pl.csanecki.memory.ui.panels.GamePanelSubscriber;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class GraphicOptionsMenu extends JMenu implements GamePanelSubscriber {

    private final GameSizeMenu gameSizeMenu;
    private final ReverseThemeMenu reverseThemeMenu;
    private final ObversesThemeMenu obversesThemeMenu;
    private final NumberOfCardsInGroupMenu numberOfCardsInGroupMenu;

    public GraphicOptionsMenu(CustomConfig customConfig) {
        super("Opcje");

        gameSizeMenu = new GameSizeMenu(customConfig);
        reverseThemeMenu = new ReverseThemeMenu(customConfig);
        obversesThemeMenu = new ObversesThemeMenu(customConfig);
        numberOfCardsInGroupMenu = new NumberOfCardsInGroupMenu(customConfig);

        add(gameSizeMenu);
        add(reverseThemeMenu);
        add(obversesThemeMenu);
        add(numberOfCardsInGroupMenu);
    }

    public void registerSubscriber(GraphicOptionsMenuSubscriber subscriber) {
        gameSizeMenu.registerSubscriber(subscriber);
        reverseThemeMenu.registerSubscriber(subscriber);
        obversesThemeMenu.registerSubscriber(subscriber);
        numberOfCardsInGroupMenu.registerSubscriber(subscriber);
    }

    @Override
    public void update(CurrentGameState gameState) {
        setEnabled(gameState == CurrentGameState.Idle);
    }

    private static class GameSizeMenu extends JMenu {

        private final Collection<GraphicOptionsMenuSubscriber> subscribers = new ArrayList<>();
        private final Collection<JMenuItem> items = new ArrayList<>();

        private GameSizeMenu(CustomConfig customConfig) {
            super("Rozmiar planszy");

            JMenuItem gameSizeMenuSmall = new JMenuItem("Mały");
            gameSizeMenuSmall.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeGameSize(GameSize.Small);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                gameSizeMenuSmall.setEnabled(false);
            }));
            JMenuItem gameSizeMenuMedium = new JMenuItem("Średni");
            gameSizeMenuMedium.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeGameSize(GameSize.Medium);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                gameSizeMenuMedium.setEnabled(false);
            }));
            JMenuItem gameSizeMenuLarge = new JMenuItem("Duży");
            gameSizeMenuLarge.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeGameSize(GameSize.Large);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                gameSizeMenuLarge.setEnabled(false);
            }));

            items.add(gameSizeMenuSmall);
            items.add(gameSizeMenuMedium);
            items.add(gameSizeMenuLarge);

            items.forEach(this::add);
        }

        private void registerSubscriber(GraphicOptionsMenuSubscriber subscriber) {
            this.subscribers.add(subscriber);
        }

    }

    private static class ReverseThemeMenu extends JMenu {

        private final Collection<GraphicOptionsMenuSubscriber> subscribers = new ArrayList<>();
        private final Collection<JMenuItem> items = new ArrayList<>();

        private ReverseThemeMenu(CustomConfig customConfig) {
            super("Motyw rewersu");

            JMenuItem reverseThemeMenuEarth = new JMenuItem("Ziemia");
            reverseThemeMenuEarth.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeReverseTheme(ReverseTheme.Earth);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                reverseThemeMenuEarth.setEnabled(false);
            }));
            JMenuItem reverseThemeMenuJungle = new JMenuItem("Dżungla");
            reverseThemeMenuJungle.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeReverseTheme(ReverseTheme.Jungle);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                reverseThemeMenuJungle.setEnabled(false);
            }));
            JMenuItem reverseThemeMenuPremierLeague = new JMenuItem("Premier League");
            reverseThemeMenuPremierLeague.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeReverseTheme(ReverseTheme.PremierLeague);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                reverseThemeMenuPremierLeague.setEnabled(false);
            }));

            items.add(reverseThemeMenuEarth);
            items.add(reverseThemeMenuJungle);
            items.add(reverseThemeMenuPremierLeague);

            items.forEach(this::add);
        }

        private void registerSubscriber(GraphicOptionsMenuSubscriber subscriber) {
            this.subscribers.add(subscriber);
        }
    }

    private static class ObversesThemeMenu extends JMenu {

        private final Collection<GraphicOptionsMenuSubscriber> subscribers = new ArrayList<>();
        private final Collection<JMenuItem> items = new ArrayList<>();

        private ObversesThemeMenu(CustomConfig customConfig) {
            super("Motyw awersu");

            JMenuItem obversesThemeMenuEnglishClubs = new JMenuItem("Kluby angielskie");
            obversesThemeMenuEnglishClubs.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeObversesTheme(ObversesTheme.EnglishClubs);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                obversesThemeMenuEnglishClubs.setEnabled(false);
            }));
            JMenuItem obversesThemeMenuAnimals = new JMenuItem("Zwierzęta");
            obversesThemeMenuAnimals.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeObversesTheme(ObversesTheme.Animals);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                obversesThemeMenuAnimals.setEnabled(false);
            }));

            items.add(obversesThemeMenuEnglishClubs);
            items.add(obversesThemeMenuAnimals);

            items.forEach(this::add);
        }

        private void registerSubscriber(GraphicOptionsMenuSubscriber subscriber) {
            this.subscribers.add(subscriber);
        }
    }

    private static class NumberOfCardsInGroupMenu extends JMenu {

        private final Collection<GraphicOptionsMenuSubscriber> subscribers = new ArrayList<>();
        private final Collection<JMenuItem> items = new ArrayList<>();

        private NumberOfCardsInGroupMenu(CustomConfig customConfig) {
            super("Ilość kart w grupie");

            JMenuItem numberOfCardsInGroupMenuTwo = new JMenuItem("Dwie");
            numberOfCardsInGroupMenuTwo.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Two);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                numberOfCardsInGroupMenuTwo.setEnabled(false);
            }));
            JMenuItem numberOfCardsInGroupMenuThree = new JMenuItem("Trzy");
            numberOfCardsInGroupMenuThree.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Three);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                numberOfCardsInGroupMenuThree.setEnabled(false);
            }));
            JMenuItem numberOfCardsInGroupMenuFour = new JMenuItem("Cztery");
            numberOfCardsInGroupMenuFour.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Four);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                numberOfCardsInGroupMenuFour.setEnabled(false);
            }));

            items.add(numberOfCardsInGroupMenuTwo);
            items.add(numberOfCardsInGroupMenuThree);
            items.add(numberOfCardsInGroupMenuFour);

            items.forEach(this::add);
        }

        private void registerSubscriber(GraphicOptionsMenuSubscriber subscriber) {
            this.subscribers.add(subscriber);
        }
    }
}
