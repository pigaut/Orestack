package io.github.pigaut.rpg.listener;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class ToolEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public ToolEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
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
        if (!player.hasPermission(plugin.getPermission("tool.use"))) {
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

        if (!player.hasPermission(plugin.getPermission("tool.use"))) {
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

        if (!player.hasPermission(plugin.getPermission("tool.use"))) {
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
            return;
        }

        if (tool.getOnRightClickEntity() != null) {
            tool.getOnRightClickEntity().accept(player, event.getRightClicked());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Tool tool = plugin.getTool(event.getOffHandItem());
        if (tool == null) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (!player.hasPermission(plugin.getPermission("tool.use"))) {
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
            return;
        }

        if (tool.getOnSwapHand() != null) {
            tool.getOnSwapHand().accept(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        Tool tool = plugin.getTool(item);
        if (tool == null) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (!player.hasPermission(plugin.getPermission("tool.use"))) {
            plugin.sendMessage(player, Context.fromPlayer(plugin, player), "missing-tool-permission");
            return;
        }

        if (tool.getOnDropItem() != null) {
            tool.getOnDropItem().accept(event.getPlayer());
        }
    }

}

