package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.setup.GameCard;
import pl.csanecki.memory.setup.GameSetupCoordinator;
import pl.csanecki.memory.setup.GroupOfGameCards;
import pl.csanecki.memory.ui.items.GraphicCard;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class GameConfig {

    private static final int ALLOWED_IMAGE_HEIGHT = GraphicCard.REQUIRED_HEIGHT;
    private static final int ALLOWED_IMAGE_WIDTH = GraphicCard.REQUIRED_WIDTH;

    private static final ReverseTheme DEFAULT_REVERSE = ReverseTheme.Jungle;
    private static final ObversesTheme DEFAULT_OBVERSES = ObversesTheme.EnglishClubs;

    private static final int DEFAULT_NUMBER_OF_ROWS = 5;
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 8;
    private static final int DEFAULT_NUMBER_OF_ITEMS = 2;

    public final int rows;
    public final int columns;

    // 100x100 px
    public final ImageIcon reverseImage;

    public final Set<Group> groups;

    public GameConfig(int rows, int columns, ReverseTheme reverseTheme, ObversesTheme obversesTheme) {
        Set<Group> groups1 = Arrays.stream((new File(getClass().getResource(obversesTheme.getPath()).getPath())).list())
            .filter(f -> f.endsWith(".png"))
            .map(f -> obversesTheme.getPath() + f)
            .map(Group::new)
            .collect(Collectors.toUnmodifiableSet());


//        int numberOfElements = countNumberOfElements(groups);
        int numberOfFields = countNumberOfFields(rows, columns);
//        if (numberOfElements != numberOfFields) {
//            throw new IllegalArgumentException("number of elements and fields must be equal");
//        }

        URL reverseImageUrl = Objects.requireNonNull(getClass().getResource(reverseTheme.getPath()));

        this.rows = rows;
        this.columns = columns;
        this.reverseImage = new ImageIcon(reverseImageUrl);
        this.groups = groups1;
    }

    public GameConfig() {
        this(DEFAULT_NUMBER_OF_ROWS, DEFAULT_NUMBER_OF_COLUMNS, DEFAULT_REVERSE, DEFAULT_OBVERSES);
    }

    public GameSetupCoordinator createGameSetupCoordinator() {
        AtomicInteger flatItemIdGenerator = new AtomicInteger(0);
        AtomicInteger flatItemsGroupsIdGenerator = new AtomicInteger(0);

        Set<GroupOfGameCards> groupToGuesses = this.groups.stream()
            .map(group -> IntStream.of(0, group.numberOfItems)
                .mapToObj(index -> FlatItemId.of(flatItemIdGenerator.getAndIncrement()))
                .map(flatItemId -> new GameCard(flatItemId, reverseImage, group.obverseImage))
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
        public final ImageIcon obverseImage;
        public final int numberOfItems;

        public Group(String obverseImagePath, int numberOfItems) {
            URL obverseImageUrl = Objects.requireNonNull(getClass().getResource(obverseImagePath));

            this.obverseImage = new ImageIcon(obverseImageUrl);
            this.numberOfItems = numberOfItems;

            if (obverseImage.getIconWidth() != ALLOWED_IMAGE_WIDTH || obverseImage.getIconHeight() != ALLOWED_IMAGE_HEIGHT) {
                throw new IllegalArgumentException(
                    String.format("image " + obverseImagePath + " must be %sx%s", ALLOWED_IMAGE_WIDTH, ALLOWED_IMAGE_HEIGHT));
            }
        }

        public Group(String obverseImagePath) {
            this(obverseImagePath, DEFAULT_NUMBER_OF_ITEMS);
        }
    }

}
