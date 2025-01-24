package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.*;

public class BlockBreakListener implements Listener {

    private final OrestackPlugin plugin;

    public BlockBreakListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final BlockGenerator generator = plugin.getBlockGenerator(block.getLocation());
        if (generator == null || generator.isUpdating()) {
            return;
        }
        final GeneratorStage stage = generator.getCurrentStage();
        final OrestackPlayer playerState = plugin.getPlayer(event.getPlayer().getUniqueId());
        playerState.updatePlaceholders(generator);

        final GeneratorHarvestEvent generatorHarvestEvent = new GeneratorHarvestEvent(playerState, block, generator, stage);
        SpigotServer.callEvent(generatorHarvestEvent);
        if (generatorHarvestEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        final Function breakFunction = stage.getBreakFunction();
        if (breakFunction != null) {
            breakFunction.run(playerState, block);
        }

        if (!stage.getState().isHarvestable()) {
            event.setCancelled(true);
        }
        else {
            generator.previousStage();
        }
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        final Block block = event.getBlock();
        final BlockGenerator generator = plugin.getBlockGenerator(block.getLocation());
        if (generator == null) {
            return;
        }
        final GeneratorStage stage = generator.getCurrentStage();
        if (!stage.isDropItems()) {
            event.setCancelled(true);
        }
    }

}
