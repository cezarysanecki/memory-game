package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.setup.GameCard;
import pl.csanecki.memory.setup.GameSetupCoordinator;
import pl.csanecki.memory.setup.GroupOfGameCards;
import pl.csanecki.memory.ui.items.GraphicCard;

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

    private static final String DEFAULT_REVERSE = "/img/reverses/earth.png";
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
                new Group("/img/averses/animals/giraffe.png"),
                new Group("/img/averses/animals/lion.png"),
                new Group("/img/averses/animals/elephant.png"),
                new Group("/img/averses/animals/hippopotamus.png"),
                new Group("/img/averses/animals/dog.png"),
                new Group("/img/averses/animals/cat.png"),
                new Group("/img/averses/animals/bear.png"),
                new Group("/img/averses/animals/crocodile.png"),
                new Group("/img/averses/animals/buffalo.png"),
                new Group("/img/averses/animals/cow.png"),
                new Group("/img/averses/animals/parrot.png"),
                new Group("/img/averses/animals/horse.png"),
                new Group("/img/averses/animals/deer.png"),
                new Group("/img/averses/animals/snake.png"),
                new Group("/img/averses/animals/tiger.png"),
                new Group("/img/averses/animals/dolphin.png"),
                new Group("/img/averses/animals/boar.png"),
                new Group("/img/averses/animals/seal.png"),
                new Group("/img/averses/animals/squirrel.png"),
                new Group("/img/averses/animals/rabbit.png")));
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
