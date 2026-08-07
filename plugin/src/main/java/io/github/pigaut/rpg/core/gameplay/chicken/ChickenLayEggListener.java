package io.github.pigaut.rpg.core.gameplay.chicken;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

public class ChickenLayEggListener implements Listener {

    private final EnhancedPlugin plugin;

    public ChickenLayEggListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Chicken chicken)) {
            return;
        }

        Settings settings = plugin.getSettings();
        if (settings.isCustomEggLaying()) {
            Delay eggLayDelay = settings.getEggLayDelay();
            chicken.setEggLayTime(eggLayDelay.toTicks());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLayEgg(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Chicken chicken)) {
            return;
        }

        Item item = event.getItemDrop();
        if (item.getItemStack().getType() != Material.EGG) {
            return;
        }

        plugin.getChickenEggs().add(item);

        Settings settings = plugin.getSettings();
        if (settings.isCustomEggLaying()) {
            Delay eggLayDelay = settings.getEggLayDelay();
            chicken.setEggLayTime(eggLayDelay.toTicks());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        plugin.getChickenEggs().remove(event.getItem());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemDespawn(ItemDespawnEvent event) {
        plugin.getChickenEggs().remove(event.getEntity());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemMerge(ItemMergeEvent event) {
        Item target = event.getTarget();
        Item source = event.getEntity();

        ChickenEggManager chickenEggs = plugin.getChickenEggs();
        if (chickenEggs.contains(source) || chickenEggs.contains(target)) {
            chickenEggs.remove(source);
            chickenEggs.add(target);
        }
    }

}