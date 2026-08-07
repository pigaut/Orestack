package io.github.pigaut.rpg.core.playerblocks;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;

public class PlayerPlacedBlockListener implements Listener {

    private final EnhancedPlugin plugin;

    public PlayerPlacedBlockListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        plugin.getPlayerPlacedBlocks().add(event.getBlockPlaced());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        // Delay to end of the tick so when listening to DropItemEvent the block is still present
        plugin.getScheduler().runTask(() -> {
            plugin.getPlayerPlacedBlocks().remove(event.getBlock());
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Block block : event.blockList()) {
            plugin.getPlayerPlacedBlocks().remove(block);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            plugin.getPlayerPlacedBlocks().remove(block);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        plugin.getPlayerPlacedBlocks().remove(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityBlockChange(EntityChangeBlockEvent event) {
        plugin.getPlayerPlacedBlocks().remove(event.getBlock());
    }

}
