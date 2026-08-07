package io.github.pigaut.rpg.listener.gameplay;

import io.github.pigaut.rpg.core.gameplay.cow.*;
import io.github.pigaut.rpg.event.farm.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.gameplay.cow.*;
import io.github.pigaut.rpg.event.farm.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class GameplayEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public GameplayEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (plugin.getSettings().isHungerHealthRegen()) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Settings settings = plugin.getSettings();
        if (!settings.isHungerDepletion()) {
            event.setCancelled(true);
            player.setFoodLevel(settings.getHungerCap());
            player.setSaturation(settings.getSaturationCap());
            return;
        }

        // Depletion enabled, but still respect the cap as a ceiling
        int hungerCap = settings.getHungerCap();
        if (event.getFoodLevel() > hungerCap) {
            event.setFoodLevel(hungerCap);
        }
    }

    @EventHandler
    public void onEntityExhaustion(EntityExhaustionEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Settings settings = plugin.getSettings();
        if (!settings.isHungerDepletion()) {
            event.setCancelled(true);
            return;
        }

        event.setExhaustion(event.getExhaustion() * settings.getExhaustionMultiplier());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(plugin, () -> {
            int saturationCap = plugin.getSettings().getSaturationCap();
            if (player.getSaturation() > saturationCap) {
                player.setSaturation(saturationCap);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Item item = event.getItem();
        if (item.getItemStack().getType() != Material.EGG) {
            return;
        }

        if (!plugin.isChickenLaidEgg(item)) {
            return;
        }

        Settings settings = plugin.getSettings();
        if (settings.isCustomEggLaying()) {
            Amount eggLayAmount = settings.getEggLayAmount();
            ItemStack egg = item.getItemStack();
            egg.setAmount(eggLayAmount.intValue());
            item.setItemStack(egg);
        }

        PlayerCollectEggEvent eggEvent = new PlayerCollectEggEvent(player, item);
        Bukkit.getPluginManager().callEvent(eggEvent);

        if (eggEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Cow cow) ) {
            return;
        }

        if (cow.getAge() < 0) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.BUCKET) {
            return;
        }

        CowMilkManager cowsMilk = plugin.getCowsMilk();

        Settings settings = plugin.getSettings();
        if (settings.isCustomCowMilking() && !cowsMilk.hasMilkLeft(cow)) {
            event.setCancelled(true);
            return;
        }

        PlayerMilkCowEvent milkEvent = new PlayerMilkCowEvent(player, cow);
        Server.callEvent(milkEvent);

        if (milkEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        if (settings.isCustomCowMilking()) {
            cowsMilk.milk(cow);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onShear(PlayerShearEntityEvent event) {
        if (!(event.getEntity() instanceof Sheep sheep)) {
            return;
        }

        Player player = event.getPlayer();

        PlayerShearSheepEvent shearEvent = new PlayerShearSheepEvent(player, sheep);
        Bukkit.getPluginManager().callEvent(shearEvent);

        if (shearEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

}
