package pl.cezarysanecki.memory.ui.menu;

import pl.cezarysanecki.memory.config.CustomConfig;
import pl.cezarysanecki.memory.config.GameSize;
import pl.cezarysanecki.memory.config.NumberOfCardsInGroup;
import pl.cezarysanecki.memory.config.ObversesTheme;
import pl.cezarysanecki.memory.config.ReverseTheme;
import pl.cezarysanecki.memory.ui.panels.CurrentGameState;
import pl.cezarysanecki.memory.ui.panels.GamePanelSubscriber;

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
            JMenuItem gameSizeMenuMedium = new JMenuItem("Średni");
            JMenuItem gameSizeMenuLarge = new JMenuItem("Duży");

            items.add(gameSizeMenuSmall);
            items.add(gameSizeMenuMedium);
            items.add(gameSizeMenuLarge);

            gameSizeMenuSmall.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeGameSize(GameSize.Small);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                gameSizeMenuSmall.setEnabled(false);
            }));
            gameSizeMenuMedium.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeGameSize(GameSize.Medium);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                gameSizeMenuMedium.setEnabled(false);
            }));
            gameSizeMenuLarge.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeGameSize(GameSize.Large);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                gameSizeMenuLarge.setEnabled(false);
            }));

            items.forEach(this::add);

            switch (customConfig.gameSize) {
                case Small -> gameSizeMenuSmall.setEnabled(false);
                case Medium -> gameSizeMenuMedium.setEnabled(false);
                case Large -> gameSizeMenuLarge.setEnabled(false);
            }
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
            JMenuItem reverseThemeMenuJungle = new JMenuItem("Dżungla");
            JMenuItem reverseThemeMenuPremierLeague = new JMenuItem("Premier League");

            items.add(reverseThemeMenuEarth);
            items.add(reverseThemeMenuJungle);
            items.add(reverseThemeMenuPremierLeague);

            reverseThemeMenuEarth.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeReverseTheme(ReverseTheme.Earth);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                reverseThemeMenuEarth.setEnabled(false);
            }));
            reverseThemeMenuJungle.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeReverseTheme(ReverseTheme.Jungle);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                reverseThemeMenuJungle.setEnabled(false);
            }));
            reverseThemeMenuPremierLeague.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeReverseTheme(ReverseTheme.PremierLeague);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                reverseThemeMenuPremierLeague.setEnabled(false);
            }));

            items.forEach(this::add);

            switch (customConfig.reverseTheme) {
                case Earth -> reverseThemeMenuEarth.setEnabled(false);
                case Jungle -> reverseThemeMenuJungle.setEnabled(false);
                case PremierLeague -> reverseThemeMenuPremierLeague.setEnabled(false);
            }
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
            JMenuItem obversesThemeMenuAnimals = new JMenuItem("Zwierzęta");

            items.add(obversesThemeMenuEnglishClubs);
            items.add(obversesThemeMenuAnimals);

            obversesThemeMenuEnglishClubs.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeObversesTheme(ObversesTheme.EnglishClubs);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                obversesThemeMenuEnglishClubs.setEnabled(false);
            }));
            obversesThemeMenuAnimals.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeObversesTheme(ObversesTheme.Animals);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                obversesThemeMenuAnimals.setEnabled(false);
            }));

            items.forEach(this::add);

            switch (customConfig.obversesTheme) {
                case EnglishClubs -> obversesThemeMenuEnglishClubs.setEnabled(false);
                case Animals -> obversesThemeMenuAnimals.setEnabled(false);
            }
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
            JMenuItem numberOfCardsInGroupMenuThree = new JMenuItem("Trzy");
            JMenuItem numberOfCardsInGroupMenuFour = new JMenuItem("Cztery");

            items.add(numberOfCardsInGroupMenuTwo);
            items.add(numberOfCardsInGroupMenuThree);
            items.add(numberOfCardsInGroupMenuFour);

            numberOfCardsInGroupMenuTwo.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Two);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                numberOfCardsInGroupMenuTwo.setEnabled(false);
            }));
            numberOfCardsInGroupMenuThree.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Three);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                numberOfCardsInGroupMenuThree.setEnabled(false);
            }));
            numberOfCardsInGroupMenuFour.addActionListener(event -> subscribers.forEach(subscriber -> {
                customConfig.changeNumberOfCardsInGroup(NumberOfCardsInGroup.Four);
                subscriber.update(customConfig);
                items.forEach(item -> item.setEnabled(true));
                numberOfCardsInGroupMenuFour.setEnabled(false);
            }));

            items.forEach(this::add);

            switch (customConfig.numberOfCardsInGroup) {
                case Two -> numberOfCardsInGroupMenuTwo.setEnabled(false);
                case Three -> numberOfCardsInGroupMenuThree.setEnabled(false);
                case Four -> numberOfCardsInGroupMenuFour.setEnabled(false);
            }
        }

        private void registerSubscriber(GraphicOptionsMenuSubscriber subscriber) {
            this.subscribers.add(subscriber);
        }
    }
}
