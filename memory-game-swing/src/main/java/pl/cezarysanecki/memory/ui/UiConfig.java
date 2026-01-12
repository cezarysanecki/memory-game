package pl.cezarysanecki.memory.ui;

import pl.cezarysanecki.memory.config.CustomConfig;
import pl.cezarysanecki.memory.config.ObversesTheme;
import pl.cezarysanecki.memory.config.ReverseTheme;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class UiConfig {

    public static final Color CARDS_PANEL_BACKGROUND_COLOR = Color.decode("#F6D59F");
    public static final Color SCORE_LABEL_BACKGROUND_COLOR = Color.decode("#FF9904");
    public static final Color SCORE_LABEL_FOREGROUND_COLOR = Color.decode("#F8FFFE");
    public static final Font GLOBAL_FONT = new Font("Courier", Font.BOLD, 15);

    private static final String ALLOWED_IMAGE_FORMAT = ".png";
    private static final int REQUIRED_IMAGE_WIDTH = 100;
    private static final int REQUIRED_IMAGE_HEIGHT = 100;
    private static final int REQUIRED_NUMBER_OF_OBVERSE_IMAGES = 20;

    public final int rows;
    public final int columns;
    public final ImageIcon reverseImage;
    public final List<ImageIcon> obverseImages;
    public final int numberOfCardsInGroup;

    private UiConfig(int rows, int columns, ImageIcon reverseImage, List<ImageIcon> obverseImages, int numberOfCardsInGroup) {
        this.rows = rows;
        this.columns = columns;
        this.reverseImage = reverseImage;
        this.obverseImages = obverseImages;
        this.numberOfCardsInGroup = numberOfCardsInGroup;
    }

    public static UiConfig create(CustomConfig customConfig) {
        ImageIcon reverseImage = resolveReverseImage(customConfig);
        List<ImageIcon> obverseImages = resolveObverseImages(customConfig);
        return new UiConfig(
                customConfig.gameSize.numberOfRows,
                customConfig.gameSize.numberOfColumns,
                reverseImage,
                obverseImages,
                customConfig.numberOfCardsInGroup.numberOfCards);
    }

    private static ImageIcon resolveReverseImage(CustomConfig customConfig) {
        ReverseTheme reverseTheme = customConfig.reverseTheme;
        ImageIcon reverseImage = Optional.ofNullable(UiConfig.class.getResource(reverseTheme.path))
                .map(ImageIcon::new)
                .orElseThrow(() -> new IllegalArgumentException("cannot find resource path: " + reverseTheme.path));
        if (imageDoesNotHaveRequiredSize(reverseImage)) {
            throw new IllegalArgumentException("reverse image must have size of " + REQUIRED_IMAGE_WIDTH + "x" + REQUIRED_IMAGE_HEIGHT);
        }
        return reverseImage;
    }

    private static List<ImageIcon> resolveObverseImages(CustomConfig customConfig) {
        ObversesTheme obversesTheme = customConfig.obversesTheme;
        List<ImageIcon> obverseImages = Optional.ofNullable(UiConfig.class.getResource(obversesTheme.path))
                .map(URL::getPath)
                .map(File::new)
                .map(File::list)
                .map(Arrays::stream)
                .stream()
                .flatMap(stringStream -> stringStream)
                .filter(file -> file.endsWith(ALLOWED_IMAGE_FORMAT))
                .map(path -> obversesTheme.path + path)
                .map(UiConfig.class::getResource)
                .filter(Objects::nonNull)
                .map(ImageIcon::new)
                .collect(Collectors.toList());
        if (obverseImages.size() != REQUIRED_NUMBER_OF_OBVERSE_IMAGES) {
            throw new IllegalArgumentException("amount of obverse images must be " + REQUIRED_NUMBER_OF_OBVERSE_IMAGES);
        }
        if (obverseImages.stream().anyMatch(UiConfig::imageDoesNotHaveRequiredSize)) {
            throw new IllegalArgumentException("obverse images must have size of " + REQUIRED_IMAGE_WIDTH + "x" + REQUIRED_IMAGE_HEIGHT);
        }
        Collections.shuffle(obverseImages);
        return obverseImages;
    }

    public int countNumbersOfCards() {
        return columns * rows;
    }

    private static boolean imageDoesNotHaveRequiredSize(ImageIcon image) {
        return image.getIconWidth() != REQUIRED_IMAGE_WIDTH || image.getIconHeight() != REQUIRED_IMAGE_HEIGHT;
    }

}
