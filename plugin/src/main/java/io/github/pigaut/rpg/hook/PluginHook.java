package io.github.pigaut.rpg.hook;

import org.bukkit.plugin.*;

public abstract class PluginHook {

    protected final Plugin plugin;

    protected PluginHook(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public boolean isEnabled() {
        return plugin.isEnabled();
    }

}
