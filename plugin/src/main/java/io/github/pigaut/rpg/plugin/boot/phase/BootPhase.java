package io.github.pigaut.rpg.plugin.boot.phase;

import org.jetbrains.annotations.*;

public interface BootPhase {

    String getNamespace();
    String getKey();

    // Server phases
    BootPhase SERVER_LOADED = server("loaded");
    BootPhase WORLDS_LOADED = server("worlds_loaded");

    // Plugin phases
    BootPhase ITEMSADDER_DATA_LOADED = plugin("ItemsAdder", "data_loaded");

    static BootPhase server(@NotNull String serverPhase) {
        return new ServerBootPhase(serverPhase);
    }

    static BootPhase plugin(@NotNull String pluginName) {
        return new PluginBootPhase(pluginName, null);
    }

    static BootPhase plugin(@NotNull String pluginName, @NotNull String pluginPhase) {
        return new PluginBootPhase(pluginName, pluginPhase);
    }

    static BootPhase pluginEnabled(@NotNull String pluginName) {
        return plugin(pluginName, "enabled");
    }

}
