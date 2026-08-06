package io.github.pigaut.rpg.hook.plotsquared;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

// Register this listener before PlotSquared is enabled to override its block-break handler (lowest priority).
public class PlotBlockBreakListener implements Listener {

    private final RpgMakerPlugin plugin;

    public PlotBlockBreakListener(RpgMakerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (plugin.getGenerators().isGenerator(block.getLocation())) {
            // Cancel so that PlotSquared block break handler ignores this event
            event.setCancelled(true);
        }
    }

}
