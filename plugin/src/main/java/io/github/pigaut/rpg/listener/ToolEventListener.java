package io.github.pigaut.rpg.listener;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class ToolEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public ToolEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();

        Tool tool = plugin.getTool(item);
        if (tool == null) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission(tool.getPermission())) {
            event.setCancelled(true);
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Tool tool = plugin.getTool(event.getItem());
        if (tool == null) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (!player.hasPermission(tool.getPermission())) {
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
            return;
        }

        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (tool.getOnLeftClickBlock() != null) {
                tool.getOnLeftClickBlock().accept(event);
            }
        } else if (action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (tool.getOnRightClickBlock() != null) {
                tool.getOnRightClickBlock().accept(event);
            }
        }
        else if (action == Action.LEFT_CLICK_AIR) {
            if (tool.getOnLeftClickAir() != null) {
                tool.getOnLeftClickAir().accept(event);
            }
        } else if (action == Action.RIGHT_CLICK_AIR) {
            if (tool.getOnRightClickAir() != null) {
                tool.getOnRightClickAir().accept(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHitEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        Tool tool = plugin.getTool(item);
        if (tool == null) {
            return;
        }

        event.setCancelled(true);

        if (!player.hasPermission(tool.getPermission())) {
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
            return;
        }

        if (tool.getOnLeftClickEntity() != null) {
            tool.getOnLeftClickEntity().accept(player, event.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Tool tool = plugin.getTool(item);
        if (tool == null) {
            return;
        }

        event.setCancelled(true);

        if (!player.hasPermission(tool.getPermission())) {
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
            return;
        }

        if (tool.getOnRightClickEntity() != null) {
            tool.getOnRightClickEntity().accept(player, event.getRightClicked());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getOffHandItem();

        Tool tool = plugin.getTool(item);
        if (tool == null) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission(tool.getPermission())) {
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
            return;
        }

        if (tool.getOnSwapHand() != null) {
            event.setCancelled(true);
            tool.getOnSwapHand().accept(player, item);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventorySwapHand(InventoryClickEvent event) {
        ClickType click = event.getClick();
        if (click != ClickType.DROP) {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        Tool tool = plugin.getTool(clickedItem);
        if (tool == null) {
            return;
        }

        if (tool.getOnDropItem() != null) {
            clickedInventory.setItem(event.getSlot(), null);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        Item itemDrop = event.getItemDrop();

        ItemStack item = itemDrop.getItemStack();
        Tool tool = plugin.getTool(item);
        if (tool == null) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission(tool.getPermission())) {
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
            return;
        }

        if (tool.getOnDropItem() != null) {
            event.setCancelled(true);
            tool.getOnDropItem().accept(event.getPlayer(), item);
            itemDrop.setItemStack(item);
        }
    }

}

