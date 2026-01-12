package pl.cezarysanecki.memory.ui.panels;

import pl.cezarysanecki.memory.engine.MemoryGameApp;
import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.GuessResult;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;
import pl.cezarysanecki.memory.ui.UiConfig;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static pl.cezarysanecki.memory.ui.UiConfig.CARDS_PANEL_BACKGROUND_COLOR;

public class CardsPanel extends JPanel {

    private static final int BOUND = 10;

    private final Collection<CardsPanelSubscriber> subscribers = new ArrayList<>();

    private final MemoryGameApp memoryGameApp = MemoryGameApp.inMemory();

    private UiConfig uiConfig;
    private MemoryGameId currentGameId;
    private List<GraphicCard> graphicCards;

    private int columns;
    private int rows;

    public CardsPanel(UiConfig uiConfig) {
        prepareCardsPanel(uiConfig);

        graphicCards.forEach(this::add);
        addMouseListener(new ClickMouseListener());

        setLayout(null);
        setLocation(0, 0);
        setBackground(CARDS_PANEL_BACKGROUND_COLOR);
    }

    public void adjustTo(UiConfig uiConfig) {
        prepareCardsPanel(uiConfig);

        removeAll();
        graphicCards.forEach(this::add);

        repaint();
    }

    public void reset() {
        MemoryGameState gameState = memoryGameApp.start(uiConfig.countNumbersOfCards(), uiConfig.numberOfCardsInGroup);
        this.currentGameId = gameState.memoryGameId();

        refreshAll(gameState);

        setGraphicCardsBounds(columns, rows, graphicCards);
        subscribers.forEach(subscriber -> subscriber.update(CurrentGameState.Idle));
    }

    private void prepareCardsPanel(UiConfig uiConfig) {
        MemoryGameState gameState = memoryGameApp.start(uiConfig.countNumbersOfCards(), uiConfig.numberOfCardsInGroup);

        List<GraphicCard> graphicCards = prepareGraphicCards(uiConfig, gameState);
        setGraphicCardsBounds(uiConfig.columns, uiConfig.rows, graphicCards);

        Dimension panelDimension = resolvePanelDimension(uiConfig, graphicCards);
        setSize(panelDimension);

        this.uiConfig = uiConfig;
        this.currentGameId = gameState.memoryGameId();
        this.graphicCards = graphicCards;
        this.columns = uiConfig.columns;
        this.rows = uiConfig.rows;
    }

    public void registerSubscriber(CardsPanelSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    private Dimension resolvePanelDimension(UiConfig uiConfig, List<GraphicCard> graphicCards) {
        Integer maxCardsX = graphicCards.stream()
                .map(JComponent::getX)
                .max(Comparator.comparingInt(x -> x))
                .orElse(0);
        Integer maxCardsY = graphicCards.stream()
                .map(JComponent::getY)
                .max(Comparator.comparingInt(y -> y))
                .orElse(0);
        int width = maxCardsX + uiConfig.reverseImage.getIconWidth() + BOUND;
        int height = maxCardsY + uiConfig.reverseImage.getIconWidth() + BOUND;

        return new Dimension(width, height);
    }

    private List<GraphicCard> prepareGraphicCards(UiConfig uiConfig, MemoryGameState gameState) {
        List<GraphicCard> graphicCards = new ArrayList<>();
        for (MemoryGameState.FlatItem flatItem : gameState.flatItems()) {
            ImageIcon obverseImage = uiConfig.obverseImages.get(flatItem.assignedGroupId().id());
            graphicCards.add(new GraphicCard(
                    flatItem.flatItemId(),
                    uiConfig.reverseImage,
                    obverseImage,
                    flatItem.obverseUp()));
        }
        return graphicCards;
    }

    private void setGraphicCardsBounds(int columns, int rows, List<GraphicCard> graphicCards) {
        int sizeCards = graphicCards.size();
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                sizeCards--;
                GraphicCard graphicCard = graphicCards.get(sizeCards);
                graphicCard.setBounds(
                        BOUND * (column + 1) + column * graphicCard.getWidth(),
                        BOUND * (row + 1) + row * graphicCard.getHeight(),
                        graphicCard.getWidth(), graphicCard.getHeight());
            }
        }
    }

    private Optional<GraphicCard> findCardByCoordinates(Point2D point) {
        return graphicCards.stream()
                .filter(graphicCard -> graphicCard.contains(point))
                .findFirst();
    }

    private void refreshAll(MemoryGameState gameState) {
        graphicCards.forEach(graphicCard -> gameState.flatItems()
                .stream()
                .filter(flatItem -> flatItem.flatItemId().equals(graphicCard.flatItemId))
                .findFirst()
                .ifPresent(graphicCard::refresh));
    }

    private class ClickMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            findCardByCoordinates(event.getPoint())
                    .ifPresent(graphicCard -> {
                        GuessResult result = memoryGameApp.turnCard(currentGameId, graphicCard.flatItemId);
                        if (result.actionResult() == GuessResult.State.Failure) {
                            graphicCard.turnToObverseUp();
                        } else {
                            refreshAll(result.state());
                        }
                        subscribers.forEach(subscriber -> subscriber.update(
                                result.state().flatItems().stream()
                                        .allMatch(MemoryGameState.FlatItem::obverseUp) ? CurrentGameState.Ended : CurrentGameState.Running));
                    });
        }
    }

    private static class GraphicCard extends JLabel {

        private final FlatItemId flatItemId;
        private final ImageIcon reverseIcon;
        private final ImageIcon obverseIcon;

        GraphicCard(FlatItemId flatItemId, ImageIcon reverseIcon, ImageIcon obverseIcon, boolean obverse) {
            setBounds(new Rectangle(reverseIcon.getIconWidth(), reverseIcon.getIconHeight()));

            this.flatItemId = flatItemId;
            this.reverseIcon = reverseIcon;
            this.obverseIcon = obverseIcon;

            setIcon(currentIcon(obverse));
        }

        void turnToObverseUp() {
            setIcon(currentIcon(true));
        }

        void refresh(MemoryGameState.FlatItem flatItem) {
            setIcon(currentIcon(flatItem.obverseUp()));
        }

        private ImageIcon currentIcon(boolean obverse) {
            return obverse ? obverseIcon : reverseIcon;
        }

        boolean contains(Point2D point) {
            Rectangle rectangle = new Rectangle(
                    this.getLocation().x, this.getLocation().y,
                    this.getWidth(), this.getHeight());
            return rectangle.contains(point);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            ImageIcon currentImageIcon = (ImageIcon) getIcon();
            g2.drawImage(currentImageIcon.getImage(), 0, 0, null);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (this == obj) return true;
            if (this.getClass() != obj.getClass()) return false;
            GraphicCard graphicCard = (GraphicCard) obj;
            return this.flatItemId.equals(graphicCard.flatItemId);
        }
    }


}
