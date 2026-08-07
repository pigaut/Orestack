package io.github.pigaut.rpg.listener.phase;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.boot.phase.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.boot.phase.*;
import org.bukkit.event.*;
import org.bukkit.event.server.*;

public class PluginPhaseListener implements Listener {

    private final EnhancedJavaPlugin plugin;

    public PluginPhaseListener(EnhancedJavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        PluginBootstrap bootstrap = plugin.getBootstrap();
        if (bootstrap.isMissingStartupRequirements()) {
            String pluginName = event.getPlugin().getName();
            plugin.getBootstrap().markReady(BootPhase.pluginEnabled(pluginName));
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        PluginBootstrap bootstrap = plugin.getBootstrap();
        if (bootstrap.isMissingStartupRequirements()) {
            String pluginName = event.getPlugin().getName();
            plugin.getBootstrap().markReady(BootPhase.plugin(pluginName));
        }
    }

}
