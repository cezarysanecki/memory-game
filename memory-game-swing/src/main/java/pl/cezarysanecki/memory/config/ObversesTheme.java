package pl.cezarysanecki.memory.config;

public enum ObversesTheme {

    Animals("/img/obverses/animals/"),
    EnglishClubs("/img/obverses/englishclubs/");

    public final String path;

    ObversesTheme(String path) {
        this.path = path;
    }

}
