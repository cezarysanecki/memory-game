package pl.csanecki.memory.config;

public enum GameSize {

    Small(4, 5),
    Medium(6, 5),
    Large(8, 5);

    public final int numberOfColumns;
    public final int numberOfRows;

    GameSize(int numberOfColumns, int numberOfRows) {
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
    }
}
