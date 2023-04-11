package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.ui.UiConfig;
import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.GuessResult;
import pl.csanecki.memory.engine.MemoryGame;
import pl.csanecki.memory.engine.state.FlatItemCurrentState;
import pl.csanecki.memory.engine.state.GroupOfFlatItemsCurrentState;
import pl.csanecki.memory.engine.state.MemoryGameCurrentState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;

public class CardsPanel extends JPanel {

    private final MemoryGame memoryGame;
    private final List<GraphicCard> graphicCards;

    private CardsPanel(MemoryGame memoryGame, List<GraphicCard> graphicCards) {
        this.memoryGame = memoryGame;
        this.graphicCards = graphicCards;

        graphicCards.forEach(this::add);
        addMouseListener(new ClickMouseListener());
    }

    public static CardsPanel render(UiConfig uiConfig) {
        MemoryGame memoryGame = MemoryGame.create(uiConfig.countNumbersOfCards(), uiConfig.numberOfCardsInGroup);
        MemoryGameCurrentState currentState = memoryGame.currentState();

        List<GraphicCard> graphicCards = prepareGraphicCards(uiConfig, currentState);

        int sizeCards = graphicCards.size();
        for (int row = 0; row < uiConfig.rows; row++) {
            for (int column = 0; column < uiConfig.columns; column++) {
                sizeCards--;
                GraphicCard graphicCard = graphicCards.get(sizeCards);
                graphicCard.setBounds(column * 110 + 10, row * 110, 100, 100);
            }
        }

        return new CardsPanel(memoryGame, graphicCards);
    }

    private static List<GraphicCard> prepareGraphicCards(UiConfig uiConfig, MemoryGameCurrentState currentState) {
        List<GraphicCard> graphicCards = new ArrayList<>();
        int index = 0;
        for (GroupOfFlatItemsCurrentState groupOfFlatItemsCurrentState : currentState.groupOfFlatItems()) {
            ImageIcon obverseImage = uiConfig.obverseImages.get(index);
            for (FlatItemCurrentState flatItemCurrentState : groupOfFlatItemsCurrentState.flatItems()) {
                graphicCards.add(new GraphicCard(
                        flatItemCurrentState.flatItemId(),
                        uiConfig.reverseImage,
                        obverseImage,
                        flatItemCurrentState.obverse()));
            }
            index++;
        }
        Collections.shuffle(graphicCards);
        return graphicCards;
    }

    private Optional<GraphicCard> findCardByCoordinates(Point2D point) {
        return graphicCards.stream()
                .filter(graphicCard -> graphicCard.contains(point))
                .findFirst();
    }

    private void refreshAll(MemoryGameCurrentState currentState) {
        graphicCards.forEach(graphicCard -> currentState.groupOfFlatItems()
                .stream()
                .map(GroupOfFlatItemsCurrentState::flatItems)
                .flatMap(Collection::stream)
                .filter(flatItem -> flatItem.flatItemId().equals(graphicCard.flatItemId))
                .findFirst()
                .ifPresent(graphicCard::refresh));
    }

    private class ClickMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            findCardByCoordinates(event.getPoint())
                    .ifPresent(graphicCard -> {
                        GuessResult result = memoryGame.turnCard(graphicCard.flatItemId);
                        if (result == GuessResult.Failure) {
                            graphicCard.turnToObverseUp();
                        } else {
                            refreshAll(memoryGame.currentState());
                        }
                    });
        }
    }

    private static class GraphicCard extends JLabel {

        private final FlatItemId flatItemId;
        private final ImageIcon reverseIcon;
        private final ImageIcon obverseIcon;

        GraphicCard(FlatItemId flatItemId, ImageIcon reverseIcon, ImageIcon obverseIcon, boolean obverse) {
            setPreferredSize(new Dimension(reverseIcon.getIconWidth(), reverseIcon.getIconHeight()));

            this.flatItemId = flatItemId;
            this.reverseIcon = reverseIcon;
            this.obverseIcon = obverseIcon;

            setIcon(currentIcon(obverse));
        }

        void turnToObverseUp() {
            setIcon(currentIcon(true));
        }

        void refresh(FlatItemCurrentState flatItem) {
            setIcon(currentIcon(flatItem.obverse()));
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
