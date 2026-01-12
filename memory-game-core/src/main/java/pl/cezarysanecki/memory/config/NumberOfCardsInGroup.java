package pl.cezarysanecki.memory.config;

public enum NumberOfCardsInGroup {

    Two(2),
    Three(3),
    Four(4);

    public final int numberOfCards;

    NumberOfCardsInGroup(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }
}
