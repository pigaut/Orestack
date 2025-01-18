package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.function.interact.block.*;
import io.github.pigaut.voxel.player.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class PlayerInteractListener implements Listener {

    private final OrestackPlugin plugin;

    public PlayerInteractListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Block clickedBlock = event.getClickedBlock();
        final Action action = event.getAction();
        final Player player = event.getPlayer();
        final ItemStack heldItem = event.getItem();
        final Generator heldGenerator = plugin.getGenerator(heldItem);
        final Location location = clickedBlock.getLocation();
        final BlockGenerator clickedGenerator = plugin.getBlockGenerator(location);

        if (heldGenerator != null) {
            if (action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                if (!player.hasPermission("orestack.generator.place")) {
                    plugin.sendMessage(player, "missing-place-permission", heldGenerator);
                    return;
                }
                final Location targetLocation = clickedBlock.getRelative(event.getBlockFace(), clickedBlock.isPassable() ? 0 : 1).getLocation();
                plugin.getGenerators().createBlockGenerator(heldGenerator, targetLocation);
                PlayerUtil.sendActionBar(player, plugin.getLang("placed-generator"));
                return;
            }

            if (action == Action.LEFT_CLICK_BLOCK && clickedGenerator != null) {
                event.setCancelled(true);
                if (!player.hasPermission("orestack.generator.break")) {
                    plugin.sendMessage(player, "missing-break-permission", heldGenerator);
                    return;
                }
                plugin.getGenerators().removeBlockGenerator(clickedGenerator.getLocation());
                PlayerUtil.sendActionBar(player, plugin.getLang("broke-generator"));
            }
            return;
        }

        if (clickedGenerator != null) {
            final OrestackPlayer orestackPlayer = plugin.getPlayer(player.getUniqueId());
            final GeneratorStage stage = clickedGenerator.getCurrentStage();
            final GeneratorInteractEvent generatorInteractEvent = new GeneratorInteractEvent(orestackPlayer, clickedBlock, clickedGenerator, stage);
            Bukkit.getPluginManager().callEvent(generatorInteractEvent);
            if (generatorInteractEvent.isCancelled()) {
                event.setCancelled(true);
                return;
            }

            final BlockClickFunction clickFunction = stage.getClickFunction();
            if (clickFunction != null) {
                clickFunction.onBlockClick(orestackPlayer, event);
            }
        }
    }

}
