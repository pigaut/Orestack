package io.github.pigaut.rpg.hook.itemsadder;

import dev.lone.itemsadder.api.Events.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.boot.phase.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.boot.phase.*;
import org.bukkit.event.*;

public class ItemsAdderPhaseListener implements Listener {

    private final EnhancedPlugin plugin;

    public ItemsAdderPhaseListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLoad(ItemsAdderLoadDataEvent event) {
        if (event.getCause() != ItemsAdderLoadDataEvent.Cause.FIRST_LOAD) {
            return;
        }

        PluginBootstrap bootstrap = plugin.getBootstrap();
        if (bootstrap.isMissingStartupRequirements()) {
            plugin.getScheduler().runTask(() ->
                    bootstrap.markReady(BootPhase.ITEMSADDER_DATA_LOADED));
        }
    }

}
