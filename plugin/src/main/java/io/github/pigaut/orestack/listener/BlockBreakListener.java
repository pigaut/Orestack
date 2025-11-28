package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.options.*;
import io.github.pigaut.orestack.util.veinminer.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.world.*;

public class BlockBreakListener implements Listener {

    private final OrestackPlugin plugin;

    public BlockBreakListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Generator generator = plugin.getGenerator(block.getLocation());
        if (generator == null) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        int expToDrop = event.getExpToDrop();

        OrestackOptionsManager options = plugin.getOrestackOptions();
        if (options.isVeinMiner()) {
            int maxVeinSize = options.getToolMaxVeinSize(player.getInventory().getItemInMainHand());
            if (maxVeinSize > 1) {
                GeneratorBlockVein.mineBlocks(generator, player, maxVeinSize, expToDrop);
                return;
            }
        }

        GeneratorBlock.mineBlock(generator, player, block, expToDrop);
    }

}
