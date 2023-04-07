package pl.csanecki.memory;

public enum Difficulty {

    Easy(10, 4, 5),
    Medium(15, 6, 5),
    Hard(20, 8, 5);

    public final int numberOfPairs;
    public final int numberOfColumns;
    public final int numberOfRows;

    Difficulty(int numberOfPairs, int numberOfColumns, int numberOfRows) {
        this.numberOfPairs = numberOfPairs;
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
    }
}
