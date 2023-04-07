package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.setup.GameCard;
import pl.csanecki.memory.setup.GameSetupCoordinator;
import pl.csanecki.memory.setup.GroupOfGameCards;

import javax.swing.*;
import java.net.URL;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class GameConfig {

    private static final int ALLOWED_IMAGE_HEIGHT = GraphicCard.REQUIRED_HEIGHT;
    private static final int ALLOWED_IMAGE_WIDTH = GraphicCard.REQUIRED_WIDTH;

    private static final String DEFAULT_REVERSE = "/img/ziemia.png";
    private static final int DEFAULT_NUMBER_OF_ROWS = 5;
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 8;
    private static final int DEFAULT_NUMBER_OF_ITEMS = 2;

    public final int rows;
    public final int columns;

    // 100x100 px
    public final ImageIcon reverseImage;

    public final Set<Group> groups;

    public GameConfig(int rows, int columns, String reverseImagePath, Set<Group> groups) {
        int numberOfElements = countNumberOfElements(groups);
        int numberOfFields = countNumberOfFields(rows, columns);
        if (numberOfElements != numberOfFields) {
            throw new IllegalArgumentException("number of elements and fields must be equal");
        }

        URL reverseImageUrl = Objects.requireNonNull(getClass().getResource(reverseImagePath));

        this.rows = rows;
        this.columns = columns;
        this.reverseImage = new ImageIcon(reverseImageUrl);
        this.groups = groups;

        if (reverseImage.getIconWidth() != ALLOWED_IMAGE_WIDTH || reverseImage.getIconHeight() != ALLOWED_IMAGE_HEIGHT) {
            throw new IllegalArgumentException(
                    String.format("image " + reverseImagePath + " must be %sx%s", ALLOWED_IMAGE_WIDTH, ALLOWED_IMAGE_HEIGHT));
        }
    }

    public GameConfig() {
        this(DEFAULT_NUMBER_OF_ROWS, DEFAULT_NUMBER_OF_COLUMNS, DEFAULT_REVERSE, Set.of(
                new Group("/img/1.png"),
                new Group("/img/2.png"),
                new Group("/img/3.png"),
                new Group("/img/4.png"),
                new Group("/img/5.png"),
                new Group("/img/6.png"),
                new Group("/img/7.png"),
                new Group("/img/8.png"),
                new Group("/img/9.png"),
                new Group("/img/10.png"),
                new Group("/img/11.png"),
                new Group("/img/12.png"),
                new Group("/img/13.png"),
                new Group("/img/14.png"),
                new Group("/img/15.png"),
                new Group("/img/16.png"),
                new Group("/img/17.png"),
                new Group("/img/18.png"),
                new Group("/img/19.png"),
                new Group("/img/20.png")));
    }

    public GameSetupCoordinator createGameSetupCoordinator() {
        AtomicInteger flatItemIdGenerator = new AtomicInteger(0);
        AtomicInteger flatItemsGroupsIdGenerator = new AtomicInteger(0);

        Set<GroupOfGameCards> groupToGuesses = this.groups.stream()
                .map(group -> IntStream.of(0, group.numberOfItems)
                        .mapToObj(index -> FlatItemId.of(flatItemIdGenerator.getAndIncrement()))
                        .map(flatItemId -> new GameCard(flatItemId, reverseImage, group.averseImage))
                        .collect(Collectors.toUnmodifiableSet()))
                .map(gameCards -> new GroupOfGameCards(
                        FlatItemsGroupId.of(flatItemsGroupsIdGenerator.getAndIncrement()), gameCards))
                .collect(Collectors.toUnmodifiableSet());
        return new GameSetupCoordinator(groupToGuesses);
    }

    private static int countNumberOfElements(Set<Group> groups) {
        return groups.stream()
                .map(group -> group.numberOfItems)
                .reduce(0, Integer::sum, Integer::sum);
    }

    private static int countNumberOfFields(int rows, int columns) {
        return columns * rows;
    }

    public static class Group {

        // 100x100 px
        public final ImageIcon averseImage;
        public final int numberOfItems;

        public Group(String averseImagePath, int numberOfItems) {
            URL averseImageUrl = Objects.requireNonNull(getClass().getResource(averseImagePath));

            this.averseImage = new ImageIcon(averseImageUrl);
            this.numberOfItems = numberOfItems;

            if (averseImage.getIconWidth() != ALLOWED_IMAGE_WIDTH || averseImage.getIconHeight() != ALLOWED_IMAGE_HEIGHT) {
                throw new IllegalArgumentException(
                        String.format("image " + averseImagePath + " must be %sx%s", ALLOWED_IMAGE_WIDTH, ALLOWED_IMAGE_HEIGHT));
            }
        }

        public Group(String averseImagePath) {
            this(averseImagePath, DEFAULT_NUMBER_OF_ITEMS);
        }
    }

}
