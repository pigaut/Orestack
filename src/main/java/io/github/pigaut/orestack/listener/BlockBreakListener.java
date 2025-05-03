package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class BlockBreakListener implements Listener {

    private final OrestackPlugin plugin;

    public BlockBreakListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreakInit(BlockBreakEvent event) {
        if (plugin.getGenerators().isGenerator(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Generator generator = plugin.getGenerator(block.getLocation());
        if (generator == null) {
            return;
        }

        if (generator.isUpdating() || generator.isHarvesting()) {
            event.setCancelled(true);
            return;
        }

        if (!generator.matchBlocks()) {
            plugin.getGenerators().removeGenerator(generator);
            return;
        }

        final OrestackPlayer playerState = plugin.getPlayer(event.getPlayer().getUniqueId());
        playerState.updatePlaceholders(generator);

        final GeneratorMineEvent generatorMineEvent = new GeneratorMineEvent(playerState, generator, block);
        SpigotServer.callEvent(generatorMineEvent);

        if (generatorMineEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        final GeneratorStage stage = generator.getCurrentStage();
        if (!stage.getState().isHarvestable()) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(false);
        generator.setHarvesting(true);

        final Integer expToDrop = stage.getExpToDrop();
        if (expToDrop != null) {
            event.setExpToDrop(expToDrop);
        }

        if (generatorMineEvent.resetStage) {
            generator.reset();
        }
        else {
            generator.previousStage();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        final Block block = event.getBlock();
        final Generator generator = plugin.getGenerator(block.getLocation());
        if (generator == null) {
            return;
        }
        final GeneratorStage stage = generator.getCurrentStage();
        event.setCancelled(!stage.isDropItems());
    }

}
