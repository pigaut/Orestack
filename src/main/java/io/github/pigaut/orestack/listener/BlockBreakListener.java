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
        if (generator == null || generator.isUpdating()) {
            return;
        }
        if (!generator.matchBlocks()) {
            plugin.getGenerators().removeGenerator(generator);
            return;
        }
        final OrestackPlayer playerState = plugin.getPlayer(event.getPlayer().getUniqueId());
        playerState.updatePlaceholders(generator);

        final GeneratorMineEvent generatorHarvestEvent = new GeneratorMineEvent(playerState, generator, block);
        SpigotServer.callEvent(generatorHarvestEvent);
        if (generatorHarvestEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        final GeneratorStage stage = generator.getCurrentStage();
        if (!stage.getState().isHarvestable()) {
            event.setCancelled(true);
        }
        else {
            event.setCancelled(false);
            final Integer expToDrop = stage.getExpToDrop();
            if (expToDrop != null) {
                event.setExpToDrop(expToDrop);
            }
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
        if (!stage.isIdle()) {
            generator.previousStage();
        }
    }

}
