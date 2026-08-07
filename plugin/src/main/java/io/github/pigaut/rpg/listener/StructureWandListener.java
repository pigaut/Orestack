package io.github.pigaut.rpg.listener;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class StructureWandListener implements Listener {

    private final EnhancedPlugin plugin;

    public StructureWandListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!event.hasBlock() || !event.hasItem() || event.getHand() != EquipmentSlot.HAND
                || !plugin.getSettings().isStructureWand(event.getItem())) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerState playerState = plugin.getPlayerState(player);
        Context context = Context.fromPlayer(plugin, player, playerState);

        if (!player.hasPermission(plugin.getPermission("structure.wand"))) {
            plugin.sendMessage(player, context, "missing-wand-permission");
            return;
        }

        Location targetLocation = event.getClickedBlock().getLocation();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            playerState.setFirstSelection(targetLocation);
            plugin.sendMessage(player, context, "selected-first-position");
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            playerState.setSecondSelection(targetLocation);
            plugin.sendMessage(player, context, "selected-second-position");
        }
    }

}
