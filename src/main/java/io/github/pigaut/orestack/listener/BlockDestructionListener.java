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
    public void onBlockBurn(BlockBurnEvent event) {
        final BlockGenerator generator = plugin.getBlockGenerator(event.getBlock().getLocation());
        if (generator != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        final List<Block> explodedBlocks = event.blockList();
        for (Block explodedBlock : explodedBlocks) {
            final BlockGenerator generator = plugin.getBlockGenerator(explodedBlock.getLocation());
            if (generator != null) {
                explodedBlocks.remove(explodedBlock);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        final List<Block> explodedBlocks = event.blockList();
        for (Block explodedBlock : explodedBlocks) {
            final BlockGenerator generator = plugin.getBlockGenerator(explodedBlock.getLocation());
            if (generator != null) {
                explodedBlocks.remove(explodedBlock);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            final BlockGenerator generator = plugin.getBlockGenerator(movedBlock.getLocation());
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            final BlockGenerator generator = plugin.getBlockGenerator(movedBlock.getLocation());
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

}
