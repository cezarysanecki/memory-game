package pl.memory;

public class CardId {

    private final int id;

    private CardId(int id) {
        this.id = id;
    }

    public static CardId of(int id) {
        return new CardId(id);
    }
}
