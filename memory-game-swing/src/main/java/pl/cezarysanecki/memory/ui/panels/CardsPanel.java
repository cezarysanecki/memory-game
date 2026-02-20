package pl.cezarysanecki.memory.ui.panels;

import pl.cezarysanecki.memory.engine.MemoryGameApp;
import pl.cezarysanecki.memory.engine.MemoryGameEvent;
import pl.cezarysanecki.memory.engine.api.FlatItemId;
import pl.cezarysanecki.memory.engine.api.FlatItemsGroupId;
import pl.cezarysanecki.memory.engine.api.MemoryGameId;
import pl.cezarysanecki.memory.engine.api.MemoryGameState;
import pl.cezarysanecki.memory.infrastructure.MemoryGameAppFactory;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static pl.cezarysanecki.memory.ui.UiConfig.CARDS_PANEL_BACKGROUND_COLOR;

public class CardsPanel extends JPanel {

    private static final int BOUND = 10;

    private final Collection<CardsPanelSubscriber> subscribers = new ArrayList<>();

    private final MemoryGameApp memoryGameApp = MemoryGameAppFactory.inMemory();

    private UiConfig uiConfig;
    private MemoryGameId currentGameId;
    private List<GraphicCard> graphicCards;

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

        subscribers.forEach(subscriber -> subscriber.update(CurrentGameState.Idle));
    }

    public void reset() {
        adjustTo(uiConfig);
    }

    private void prepareCardsPanel(UiConfig uiConfig) {
        removeAll();

        MemoryGameState gameState = memoryGameApp.start(
                uiConfig.countNumbersOfCards(),
                uiConfig.numberOfCardsInGroup
        );

        this.uiConfig = uiConfig;
        this.currentGameId = gameState.memoryGameId();
        this.graphicCards = prepareGraphicCards(uiConfig, gameState.flatItems());

        setGraphicCardsBounds(uiConfig.columns, uiConfig.rows, graphicCards);
        Dimension panelDimension = resolvePanelDimension(uiConfig, graphicCards);
        setSize(panelDimension);

        this.graphicCards.forEach(this::add);

        repaint();
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

    private List<GraphicCard> prepareGraphicCards(UiConfig uiConfig, Set<MemoryGameState.FlatItem> flatItems) {
        Map<FlatItemsGroupId, ImageIcon> groupIdToObserveImageIcon = new HashMap<>();

        return flatItems.stream()
                .map(flatItem -> {
                    FlatItemsGroupId flatItemsGroupId = flatItem.assignedGroupId();
                    FlatItemId flatItemId = flatItem.flatItemId();
                    boolean obverseUp = flatItem.obverseUp();

                    ImageIcon obverseImage = groupIdToObserveImageIcon.get(flatItemsGroupId);
                    if (obverseImage == null) {
                        obverseImage = firstObverseIcon(uiConfig, groupIdToObserveImageIcon);
                        groupIdToObserveImageIcon.put(flatItemsGroupId, obverseImage);
                    }

                    return new GraphicCard(
                            flatItemId,
                            uiConfig.reverseImage,
                            obverseImage,
                            obverseUp);
                })
                .toList();
    }

    private ImageIcon firstObverseIcon(UiConfig uiConfig, Map<FlatItemsGroupId, ImageIcon> groupIdToObserveImageIcon) {
        for (ImageIcon obverseImage : uiConfig.obverseImages) {
            if (!groupIdToObserveImageIcon.containsValue(obverseImage)) {
                return obverseImage;
            }
        }

        throw new IllegalStateException("Free obverse image not found");
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
                        Optional<MemoryGameEvent> result = memoryGameApp.turnCard(currentGameId, graphicCard.flatItemId);
                        MemoryGameState state = memoryGameApp.getState(currentGameId);

                        result.filter(gameEvent -> gameEvent instanceof MemoryGameEvent.Missed)
                                .ifPresentOrElse(
                                        gameEvent -> graphicCard.turnToObverseUp(),
                                        () -> refreshAll(state)
                                );

                        subscribers.forEach(subscriber -> subscriber.update(state.ended() ? CurrentGameState.Ended : CurrentGameState.Running));
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
