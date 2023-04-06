package pl.csanecki.memory;

import java.util.Set;

public final class GameConfig {

    public int rows = 5;
    public int columns = 8;

    // 100x100 px
    public String reverseImage = "/img/ziemia.png";

    public Set<Group> groups = Set.of(
        new Group("/img/1.png"),
        new Group("/img/2.png"),
        new Group("/img/3.png"),
        new Group("/img/4.png"),
        new Group("/img/5.png"),
        new Group("/img/6.png"),
        new Group("/img/7.png"),
        new Group("/img/8.png"),
        new Group("/img/9.png"),
        new Group("/img/10.png"),
        new Group("/img/11.png"),
        new Group("/img/12.png"),
        new Group("/img/13.png"),
        new Group("/img/14.png"),
        new Group("/img/15.png"),
        new Group("/img/16.png"),
        new Group("/img/17.png"),
        new Group("/img/18.png"),
        new Group("/img/19.png"),
        new Group("/img/20.png"));

    static Integer countNumberOfElements(GameConfig gameConfig) {
            return gameConfig.groups
                    .stream()
                    .map(group -> group.numberOfItems)
                    .reduce(0, Integer::sum, Integer::sum);
        }

    static int countNumberOfFields(GameConfig gameConfig) {
            return gameConfig.columns * gameConfig.rows;
        }

    public static class Group {

        // 100x100 px
        public String averseImage;
        public int numberOfItems = 2;

        public Group(String averseImage, int numberOfItems) {
            this.averseImage = averseImage;
            this.numberOfItems = numberOfItems;
        }

        public Group(String averseImage) {
            this.averseImage = averseImage;
        }
    }

}
