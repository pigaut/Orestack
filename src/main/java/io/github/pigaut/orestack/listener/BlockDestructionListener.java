package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;

import java.util.*;

public class BlockDestructionListener implements Listener {

    private final OrestackPlugin plugin;

    public BlockDestructionListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (plugin.getGenerators().isGenerator(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFade(BlockFadeEvent event) {
        if (plugin.getGenerators().isGenerator(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (plugin.getGenerators().isGenerator(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        final List<Block> explodedBlocks = event.blockList();
        for (Block explodedBlock : explodedBlocks) {
            if (plugin.getGenerators().isGenerator(explodedBlock.getLocation())) {
                explodedBlocks.remove(explodedBlock);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        final List<Block> explodedBlocks = event.blockList();
        for (Block explodedBlock : explodedBlocks) {
            if (plugin.getGenerators().isGenerator(explodedBlock.getLocation())) {
                explodedBlocks.remove(explodedBlock);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            if (plugin.getGenerators().isGenerator(movedBlock.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            if (plugin.getGenerators().isGenerator(movedBlock.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

}
