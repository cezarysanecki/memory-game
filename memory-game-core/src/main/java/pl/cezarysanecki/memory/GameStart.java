package pl.cezarysanecki.memory;

import pl.cezarysanecki.memory.config.CustomConfig;
import pl.cezarysanecki.memory.ui.MemoryGameFrame;

public class GameStart {

    public static void main(String[] args) {
        CustomConfig customConfig = CustomConfig.defaultConfig();
        new MemoryGameFrame(customConfig);
    }
}
