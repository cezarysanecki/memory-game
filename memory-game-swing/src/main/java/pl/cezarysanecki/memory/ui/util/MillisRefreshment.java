package pl.cezarysanecki.memory.ui.util;

public enum MillisRefreshment {
    Ten(10), OneHundred(100), OneThousand(1_000);

    public final int millis;

    MillisRefreshment(int millis) {
        if (millis <= 0) {
            throw new IllegalArgumentException("millis cannot be negative");
        }
        this.millis = millis;
    }
}
