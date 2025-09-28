package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.voxel.bukkit.*;
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Generator generator = plugin.getGenerator(block.getLocation());
        if (generator == null) {
            return;
        }

        event.setCancelled(true);
        if (generator.isUpdating()) {
            return;
        }

        if (!generator.matchBlocks()) {
            plugin.getGenerators().unregisterGenerator(generator);
            return;
        }

        GeneratorStage stage = generator.getCurrentStage();
        if (stage.getDecorativeBlocks().contains(block.getType())) {
            return;
        }

        Player player = event.getPlayer();
        OrestackPlayer playerState = plugin.getPlayerState(player);
        playerState.updatePlaceholders(generator);

        GeneratorMineEvent generatorMineEvent = new GeneratorMineEvent(playerState, generator, block);
        SpigotServer.callEvent(generatorMineEvent);
        if (generatorMineEvent.isCancelled()) {
            return;
        }

        if (!stage.getState().isHarvestable()) {
            return;
        }

        if (stage.isDropItems()) {
            Location location = block.getLocation().add(0.5, 1, 0.5);
            for (ItemStack itemDrop : block.getDrops(player.getInventory().getItemInMainHand())) {
                ItemDrop.spawn(location, itemDrop, itemDrop.getAmount());
            }
        }

        if (stage.isDropExp()) {
            ExpDrop.spawn(block.getLocation(), event.getExpToDrop());
        }

        if (!generatorMineEvent.isStayStage()) {
            generator.previousStage();
        }
    }

}
