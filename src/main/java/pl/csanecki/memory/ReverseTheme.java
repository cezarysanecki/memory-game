package pl.csanecki.memory;

public enum ReverseTheme {

    Earth("/img/reverses/earth.png");

    private final String path;

    ReverseTheme(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
