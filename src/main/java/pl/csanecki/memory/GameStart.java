package pl.csanecki.memory;

import pl.csanecki.memory.config.CustomConfig;
import pl.csanecki.memory.ui.UiConfig;
import pl.csanecki.memory.ui.MemoryGameFrame;

public class GameStart {

    public static void main(String[] args) {
        CustomConfig customConfig = CustomConfig.defaultConfig();
        new MemoryGameFrame(customConfig);
    }
}
