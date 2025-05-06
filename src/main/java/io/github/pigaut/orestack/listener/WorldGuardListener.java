package io.github.pigaut.orestack.listener;

import com.sk89q.worldguard.bukkit.event.block.*;
import io.github.pigaut.orestack.*;
import org.bukkit.block.*;
import org.bukkit.event.*;

import java.util.*;

public class WorldGuardListener implements Listener {

    private final OrestackPlugin plugin;

    public WorldGuardListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreakBlockInit(BreakBlockEvent event) {
        final List<Block> blocks = event.getBlocks();

        if (blocks.size() != 1) {
            return;
        }

        if (plugin.getGenerators().isGenerator(blocks.get(0).getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakBlock(BreakBlockEvent event) {
        final List<Block> blocks = event.getBlocks();

        if (blocks.size() != 1) {
            return;
        }

        if (plugin.getGenerators().isGenerator(blocks.get(0).getLocation())) {
            event.setAllowed(true);
        }
    }

}
