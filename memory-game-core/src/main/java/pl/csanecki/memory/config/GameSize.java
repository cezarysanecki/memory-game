package pl.csanecki.memory.config;

public enum GameSize {

    Small(4, 3),
    Medium(4, 6),
    Large(6, 6);

    public final int numberOfColumns;
    public final int numberOfRows;

    GameSize(int numberOfColumns, int numberOfRows) {
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
    }
}
