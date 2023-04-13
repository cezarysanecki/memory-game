package pl.csanecki.memory.ui.panels;

import pl.csanecki.memory.config.CustomConfig;

public interface MenuBarSubscriber {

    void update(MenuOption menuOption);

    void update(CustomConfig customConfig);

}
