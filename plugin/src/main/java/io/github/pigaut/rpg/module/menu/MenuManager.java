package io.github.pigaut.rpg.module.menu;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;

public class MenuManager extends ConfigBackedManager<Menu> {

    public MenuManager(EnhancedJavaPlugin plugin) {
        super(plugin, Module.MENUS, Menu.class);
        ignore("buttons.yml");
    }

}
