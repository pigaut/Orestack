package io.github.pigaut.rpg.listener.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.event.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import java.util.*;

public class PlayerEquipmentChangeEventListener implements Listener {

    private final EnhancedPlugin plugin;
    private final Map<UUID, Map<EquipmentSlot, ItemStack>> lastEquipment = new HashMap<>();

    public PlayerEquipmentChangeEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    private Map<EquipmentSlot, ItemStack> snapshot(Player player) {
        PlayerInventory inventory = player.getInventory();
        Map<EquipmentSlot, ItemStack> equipment = new EnumMap<>(EquipmentSlot.class);
        equipment.put(EquipmentSlot.HEAD, cloneOrNull(inventory.getHelmet()));
        equipment.put(EquipmentSlot.CHEST, cloneOrNull(inventory.getChestplate()));
        equipment.put(EquipmentSlot.LEGS, cloneOrNull(inventory.getLeggings()));
        equipment.put(EquipmentSlot.FEET, cloneOrNull(inventory.getBoots()));
        equipment.put(EquipmentSlot.HAND, cloneOrNull(inventory.getItemInMainHand()));
        equipment.put(EquipmentSlot.OFF_HAND, cloneOrNull(inventory.getItemInOffHand()));
        return equipment;
    }

    private ItemStack cloneOrNull(ItemStack item) {
        return item != null ? item.clone() : null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        lastEquipment.put(event.getPlayer().getUniqueId(), snapshot(event.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastEquipment.remove(event.getPlayer().getUniqueId());
    }

    private void checkChange(Player player) {
        UUID playerId = player.getUniqueId();
        Map<EquipmentSlot, ItemStack> old = lastEquipment.get(playerId);
        if (old == null) {
            return;
        }

        Map<EquipmentSlot, ItemStack> current = snapshot(player);

        for (EquipmentSlot slot : PlayerUtil.EQUIPMENT_SLOTS) {
            ItemStack oldItem = old.get(slot);
            ItemStack newItem = current.get(slot);

            if (Objects.equals(oldItem, newItem)) {
                continue;
            }

            PlayerEquipmentChangeEvent equipmentChangeEvent = new PlayerEquipmentChangeEvent(
                    player, slot,
                    oldItem == null ? new ItemStack(Material.AIR) : oldItem.clone(),
                    newItem == null ? new ItemStack(Material.AIR) : newItem.clone()
            );
            Server.callEvent(equipmentChangeEvent);
        }

        lastEquipment.put(playerId, current);
    }

    private void scheduleCheck(Player player) {
        plugin.getScheduler().runTask(() -> checkChange(player));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            scheduleCheck(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            scheduleCheck(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemHeld(PlayerItemHeldEvent event) {
        scheduleCheck(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemDrop(PlayerDropItemEvent event) {
        scheduleCheck(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            scheduleCheck(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemGive(PlayerGiveItemEvent event) {
        scheduleCheck(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemTake(PlayerRemoveItemEvent event) {
        scheduleCheck(event.getPlayer());
    }

}
