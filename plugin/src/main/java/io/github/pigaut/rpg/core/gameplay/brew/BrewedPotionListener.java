package io.github.pigaut.rpg.core.gameplay.brew;

import io.github.pigaut.rpg.event.player.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.*;

public class BrewedPotionListener implements Listener {

    private final EnhancedPlugin plugin;

    public BrewedPotionListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBrew(BrewEvent event) {
        Block block = event.getBlock();
        List<ItemStack> results = event.getResults();

        for (int slot = 0; slot < results.size(); slot++) {
            ItemStack result = results.get(slot);
            if (result != null && result.getType() != Material.AIR) {
                plugin.getBrewedPotions().add(block, slot);
            } else {
                plugin.getBrewedPotions().remove(block, slot);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof BrewerInventory inventory)) {
            return;
        }

        int slot = event.getRawSlot();
        if (slot < 0 || slot > 2) return;

        BrewingStand brewingStand = inventory.getHolder();
        if (brewingStand == null) {
            return;
        }

        Block block = brewingStand.getBlock();
        if (!plugin.getBrewedPotions().isBrewedSlot(block, slot)) {
            return;
        }

        ItemStack current = event.getCurrentItem();
        if (current == null || current.getType() == Material.AIR) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        int taken = event.isShiftClick() ? current.getAmount() : 1;

        PlayerCollectBrewedPotionEvent collectEvent = new PlayerCollectBrewedPotionEvent(
                player, block, current.clone(), taken);
        Bukkit.getPluginManager().callEvent(collectEvent);

        if (collectEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        if (taken >= current.getAmount()) {
            plugin.getBrewedPotions().remove(block, slot);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getInventory() instanceof BrewerInventory inventory)) {
            return;
        }

        BrewingStand brewingStand = inventory.getHolder();
        if (brewingStand == null) {
            return;
        }

        for (int slot : event.getRawSlots()) {
            if (slot >= 0 && slot <= 2) {
                plugin.getBrewedPotions().remove(brewingStand.getBlock(), slot);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (!(event.getSource() instanceof BrewerInventory inventory)) {
            return;
        }

        BrewingStand brewingStand = inventory.getHolder();
        if (brewingStand == null) {
            return;
        }

        Block block = brewingStand.getBlock();
        plugin.getScheduler().runTask(() -> {
            for (int slot = 0; slot <= 2; slot++) {
                if (plugin.getBrewedPotions().isBrewedSlot(block, slot)) {
                    ItemStack item = inventory.getItem(slot);
                    if (item == null || item.getType() == Material.AIR) {
                        plugin.getBrewedPotions().remove(block, slot);
                    }
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.BREWING_STAND) {
            return;
        }
        plugin.getBrewedPotions().removeAll(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (block.getType() == Material.BREWING_STAND) {
                plugin.getBrewedPotions().removeAll(block);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (block.getType() == Material.BREWING_STAND) {
                plugin.getBrewedPotions().removeAll(block);
            }
        }
    }
}