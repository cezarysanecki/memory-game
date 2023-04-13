package pl.csanecki.memory.config;

public final class CustomConfig {

    public ReverseTheme reverseTheme;
    public ObversesTheme obversesTheme;
    public GameSize gameSize;
    public NumberOfCardsInGroup numberOfCardsInGroup;

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

    public void changeReverseTheme(ReverseTheme reverseTheme) {
        this.reverseTheme = reverseTheme;
    }

    public void changeObversesTheme(ObversesTheme obversesTheme) {
        this.obversesTheme = obversesTheme;
    }

    public void changeGameSize(GameSize gameSize) {
        this.gameSize = gameSize;
    }

    public void changeNumberOfCardsInGroup(NumberOfCardsInGroup numberOfCardsInGroup) {
        this.numberOfCardsInGroup = numberOfCardsInGroup;
    }

}
