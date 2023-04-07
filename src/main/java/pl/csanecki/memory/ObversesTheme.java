package pl.csanecki.memory;

public enum ObversesTheme {

    Animals("/img/obverses/animals/"),
    EnglishClubs("/img/obverses/englishclubs/");

    private final String path;

    ObversesTheme(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
