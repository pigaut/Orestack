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
        final Generator generator = plugin.getGenerator(event.getBlock().getLocation());
        if (generator != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        final List<Block> explodedBlocks = event.blockList();
        for (Block explodedBlock : explodedBlocks) {
            final Generator generator = plugin.getGenerator(explodedBlock.getLocation());
            if (generator != null) {
                explodedBlocks.remove(explodedBlock);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        final List<Block> explodedBlocks = event.blockList();
        for (Block explodedBlock : explodedBlocks) {
            final Generator generator = plugin.getGenerator(explodedBlock.getLocation());
            if (generator != null) {
                explodedBlocks.remove(explodedBlock);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            final Generator generator = plugin.getGenerator(movedBlock.getLocation());
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block movedBlock : event.getBlocks()) {
            final Generator generator = plugin.getGenerator(movedBlock.getLocation());
            if (generator != null) {
                event.setCancelled(true);
            }
        }
    }

}
