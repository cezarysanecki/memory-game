package pl.cezarysanecki.memory.config;

public enum ReverseTheme {

    Earth("/img/reverses/earth.png"),
    Jungle("/img/reverses/jungle.png"),
    PremierLeague("/img/reverses/premier_league.png");

    public final String path;

    ReverseTheme(String path) {
        this.path = path;
    }

}
