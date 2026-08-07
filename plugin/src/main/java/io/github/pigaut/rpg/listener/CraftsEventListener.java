package io.github.pigaut.rpg.listener;

import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;

public class CraftsEventListener implements Listener {

//    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//    public void onInteract(PlayerInteractEvent event) {
//        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
//            return;
//        }
//
//        Block block = event.getClickedBlock();
//        if (block == null || block.getType() != Material.BREWING_STAND) {
//            return;
//        }
//
//        Player player = event.getPlayer();
//        PlayerPotionBrewEvent brewEvent = new PlayerPotionBrewEvent(player, (BrewingStand) block.getState());
//
//        Bukkit.getPluginManager().callEvent(brewEvent);
//        if (brewEvent.isCancelled()) {
//            event.setCancelled(true);
//        }
//    }

}
