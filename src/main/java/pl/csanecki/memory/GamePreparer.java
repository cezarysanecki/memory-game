package pl.csanecki.memory;

import pl.csanecki.memory.engine.FlatItemId;
import pl.csanecki.memory.engine.FlatItemsGroupId;
import pl.csanecki.memory.setup.GameCard;
import pl.csanecki.memory.setup.GameSetupCoordinator;
import pl.csanecki.memory.setup.GroupOfGameCards;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class GamePreparer {


    public final int rows;
    public final int columns;
    public final ImageIcon reverseImage;
    public final Set<Group> groups;

    private GamePreparer(int numberOfRows, int numberOfColumns, ImageIcon reverseImage, Set<Group> groups) {
        this.rows = numberOfRows;
        this.columns = numberOfColumns;
        this.reverseImage = reverseImage;
        this.groups = groups;
    }

    public static GamePreparer create() {
        URL resource = Objects.requireNonNull(GamePreparer.class.getResource(GameConfig.OBVERSES_THEME.getPath()));
        File file = new File(resource.getPath());
        String[] array = Objects.requireNonNull(file.list());

        Set<Group> groups = Arrays.stream(array)
            .filter(f -> f.endsWith(".png"))
            .map(f -> GameConfig.OBVERSES_THEME.getPath() + f)
            .map(Group::of)
            .collect(Collectors.toUnmodifiableSet());
        URL reverseImageUrl = Objects.requireNonNull(GamePreparer.class.getResource(GameConfig.REVERSE_THEME.getPath()));

        return new GamePreparer(
            GameConfig.DIFFICULTY.numberOfRows,
            GameConfig.DIFFICULTY.numberOfColumns,
            new ImageIcon(reverseImageUrl),
            groups);
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

    public static class Group {

        private static final int DEFAULT_NUMBER_OF_ITEMS = 2;

        // 100x100 px
        public final ImageIcon obverseImage;
        public final int numberOfItems;

        private Group(ImageIcon obverseImage, int numberOfItems) {
            this.obverseImage = obverseImage;
            this.numberOfItems = numberOfItems;
        }

        private static Group of(String obverseImagePath) {
            URL obverseImageUrl = Objects.requireNonNull(Group.class.getResource(obverseImagePath));
            return new Group(new ImageIcon(obverseImageUrl), DEFAULT_NUMBER_OF_ITEMS);
        }
    }

}
