package pl.csanecki.memory.config;

public final class UserGameConfig {

    public final ReverseTheme reverseTheme;
    public final ObversesTheme obversesTheme;
    public final GameSize gameSize;
    public final NumberOfCardsInGroup numberOfCardsInGroup;

    private UserGameConfig(ReverseTheme reverseTheme, ObversesTheme obversesTheme, GameSize gameSize, NumberOfCardsInGroup numberOfCardsInGroup) {
        this.reverseTheme = reverseTheme;
        this.obversesTheme = obversesTheme;
        this.gameSize = gameSize;
        this.numberOfCardsInGroup = numberOfCardsInGroup;
    }

    public static UserGameConfig defaultConfig() {
        return new UserGameConfig(
            ReverseTheme.PremierLeague,
            ObversesTheme.Animals,
            GameSize.Small,
            NumberOfCardsInGroup.Two);
    }

    public static UserGameConfig userConfig(
        ReverseTheme reverseTheme,
        ObversesTheme obversesTheme,
        GameSize gameSize,
        NumberOfCardsInGroup numberOfCardsInGroup) {
        return new UserGameConfig(
            reverseTheme,
            obversesTheme,
            gameSize,
            numberOfCardsInGroup);
    }

}
