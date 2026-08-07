package io.github.pigaut.rpg.module.menu.entries;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MenuEntriesManager extends Manager {

    private final Map<String, MenuEntries> menuEntriesByName = new HashMap<>();

    public MenuEntriesManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    public boolean contains(@NotNull String name) {
        return menuEntriesByName.containsKey(name);
    }

    public @Nullable MenuEntries get(@NotNull String name) {
        return menuEntriesByName.get(name);
    }

    public void register(@NotNull String name, @NotNull MenuEntries menuEntries) {
        menuEntriesByName.put(name, menuEntries);
    }

    public void unregister(@NotNull String name) {
        menuEntriesByName.remove(name);
    }

}
