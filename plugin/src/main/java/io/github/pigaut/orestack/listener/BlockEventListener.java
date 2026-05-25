package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;

import java.util.*;

public class BlockEventListener implements Listener {

    private final OrestackPlugin plugin;

    public BlockEventListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        Location blockLocation = event.getBlock().getLocation();
        if (plugin.getGenerators().isGenerator(blockLocation) || plugin.getGates().isGate(blockLocation)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFade(BlockFadeEvent event) {
        Location blockLocation = event.getBlock().getLocation();
        if (plugin.getGenerators().isGenerator(blockLocation) || plugin.getGates().isGate(blockLocation)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        Location blockLocation = event.getBlock().getLocation();
        if (plugin.getGenerators().isGenerator(blockLocation) || plugin.getGates().isGate(blockLocation)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        List<Block> explodedBlocks = event.blockList();
        explodedBlocks.removeIf(explodedBlock -> {
            Location loc = explodedBlock.getLocation();
            return plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        List<Block> explodedBlocks = event.blockList();
        explodedBlocks.removeIf(explodedBlock -> {
            Location loc = explodedBlock.getLocation();
            return plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            Location loc = movedBlock.getLocation();
            if (plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc)) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            Location loc = movedBlock.getLocation();
            if (plugin.getGenerators().isGenerator(loc) || plugin.getGates().isGate(loc)) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
