package pl.csanecki.memory.config;

public final class CustomConfig {

    public final ReverseTheme reverseTheme;
    public final ObversesTheme obversesTheme;
    public final GameSize gameSize;
    public final NumberOfCardsInGroup numberOfCardsInGroup;

    private CustomConfig(ReverseTheme reverseTheme, ObversesTheme obversesTheme, GameSize gameSize, NumberOfCardsInGroup numberOfCardsInGroup) {
        this.reverseTheme = reverseTheme;
        this.obversesTheme = obversesTheme;
        this.gameSize = gameSize;
        this.numberOfCardsInGroup = numberOfCardsInGroup;
    }

    public static CustomConfig defaultConfig() {
        return new CustomConfig(
            ReverseTheme.Jungle,
            ObversesTheme.EnglishClubs,
            GameSize.Medium,
            NumberOfCardsInGroup.Three);
    }

    public static CustomConfig userConfig(
        ReverseTheme reverseTheme,
        ObversesTheme obversesTheme,
        GameSize gameSize,
        NumberOfCardsInGroup numberOfCardsInGroup) {
        return new CustomConfig(
            reverseTheme,
            obversesTheme,
            gameSize,
            numberOfCardsInGroup);
    }

}
