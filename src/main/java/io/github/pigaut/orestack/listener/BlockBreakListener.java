package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.function.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class BlockBreakListener implements Listener {

    private final OrestackPlugin plugin;

    public BlockBreakListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final BlockGenerator generator = plugin.getBlockGenerator(block.getLocation());
        if (generator == null) {
            return;
        }
        event.setCancelled(true);
        final GeneratorStage stage = generator.getCurrentStage();
        final OrestackPlayer player = plugin.getPlayer(event.getPlayer().getUniqueId());
        final GeneratorHarvestEvent generatorHarvestEvent = new GeneratorHarvestEvent(player, block, generator, stage);
        Bukkit.getPluginManager().callEvent(generatorHarvestEvent);
        if (generatorHarvestEvent.isCancelled()) {
            return;
        }
        Function.runAll(player, block, stage.getOnHarvest());
        if (stage.getState().isHarvestable()) {
            generator.previousStage();
            if (stage.isDropItems()) {
                block.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
            }
        }
    }

}
