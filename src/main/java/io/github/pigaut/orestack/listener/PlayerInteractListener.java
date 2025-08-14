package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.core.function.interact.block.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.server.*;
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

    @EventHandler
    public void onGeneratorItemClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final ItemStack heldItem = event.getItem();
        if (heldItem == null) {
            return;
        }

        final GeneratorTemplate heldGenerator = GeneratorTools.getGeneratorFromTool(heldItem);
        if (heldGenerator == null) {
            return;
        }

        final Player player = event.getPlayer();
        final Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR && player.isSneaking()) {
            event.setCancelled(true);
            if (!player.hasPermission("orestack.generator.rotate")) {
                plugin.sendMessage(player, "cannot-rotate-generator", heldGenerator);
                return;
            }
            GeneratorTools.incrementToolRotation(heldItem);
            PlayerUtil.sendActionBar(player, plugin.getLang("changed-generator-rotation"));
            return;
        }

        final Block clickedBlock = event.getClickedBlock();

        if (action == Action.LEFT_CLICK_BLOCK) {
            final Generator clickedGenerator = plugin.getGenerator(clickedBlock.getLocation());
            if (clickedGenerator == null || !clickedGenerator.getTemplate().equals(heldGenerator)) {
                return;
            }

            event.setCancelled(true);
            if (!player.hasPermission("orestack.generator.break")) {
                plugin.sendMessage(player, "cannot-break-generator", heldGenerator);
                return;
            }
            plugin.getGenerators().unregisterGenerator(clickedGenerator);
            PlayerUtil.sendActionBar(player, plugin.getLang("broke-generator"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorPlace(BlockPlaceEvent event) {
        final ItemStack heldItem = event.getItemInHand();
        final GeneratorTemplate heldGenerator = GeneratorTools.getGeneratorFromTool(heldItem);
        if (heldGenerator == null) {
            return;
        }

        final Player player = event.getPlayer();
        if (!player.hasPermission("orestack.generator.place")) {
            plugin.sendMessage(player, "cannot-place-generator", heldGenerator);
            event.setCancelled(true);
            return;
        }

        final Block blockPlaced = event.getBlockPlaced();
        final Location targetLocation = blockPlaced.getLocation();

        plugin.getScheduler().runTaskLater(1, () -> {
            blockPlaced.setType(Material.AIR, false);

            final ItemStack placedItem = heldItem.clone();
            placedItem.setAmount(1);

            try {
                Generator.create(heldGenerator, targetLocation, GeneratorTools.getToolRotation(heldItem));
                PlayerUtil.sendActionBar(player, plugin.getLang("placed-generator"));
            }
            catch (GeneratorOverlapException e) {
                PlayerUtil.giveItemsOrDrop(player, placedItem);
                PlayerUtil.sendActionBar(player, plugin.getLang("generator-overlap"));
            }
            catch (GeneratorLimitException e) {
                PlayerUtil.giveItemsOrDrop(player, placedItem);
                PlayerUtil.sendActionBar(player, plugin.getLang("large-generator-limit"));
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Generator clickedGenerator = plugin.getGenerator(event.getClickedBlock().getLocation());
        if (clickedGenerator == null) {
            return;
        }

        if (!clickedGenerator.matchBlocks()) {
            plugin.getGenerators().unregisterGenerator(clickedGenerator);
            return;
        }

        final OrestackPlayer playerState = plugin.getPlayerState(event.getPlayer().getUniqueId());
        playerState.updatePlaceholders(clickedGenerator);

        final GeneratorInteractEvent generatorInteractEvent = new GeneratorInteractEvent(playerState, clickedGenerator);
        SpigotServer.callEvent(generatorInteractEvent);
        if (generatorInteractEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        final GeneratorStage stage = clickedGenerator.getCurrentStage();
        final BlockClickFunction clickFunction = stage.getClickFunction();
        if (clickFunction != null) {
            clickFunction.onBlockClick(playerState, event);
        }
    }

}
