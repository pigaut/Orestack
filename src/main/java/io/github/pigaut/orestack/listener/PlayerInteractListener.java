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
    public void onWandClick(PlayerInteractEvent event) {
        if (!event.hasBlock() || !event.hasItem()
                || event.getHand() != EquipmentSlot.HAND
                || !GeneratorTools.isWandTool(event.getItem())) {
            return;
        }
        event.setCancelled(true);

        final Player player = event.getPlayer();
        final OrestackPlayer playerState = plugin.getPlayer(player.getUniqueId());

        if (!player.hasPermission(plugin.getLang("wand-permission"))) {
            plugin.sendMessage(player, "missing-wand-permission");
            return;
        }

        final Location targetLocation = event.getClickedBlock().getLocation();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            playerState.setFirstSelection(targetLocation);
            plugin.sendMessage(player, "selected-first-position");
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            playerState.setSecondSelection(targetLocation);
            plugin.sendMessage(player, "selected-second-position");
        }
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
            if (!player.hasPermission(plugin.getLang("generator-rotate-permission"))) {
                plugin.sendMessage(player, "missing-rotate-permission", heldGenerator);
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
            if (!player.hasPermission(plugin.getLang("generator-break-permission"))) {
                plugin.sendMessage(player, "missing-break-permission", heldGenerator);
                return;
            }
            plugin.getGenerators().removeGenerator(clickedGenerator);
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
        if (!player.hasPermission(plugin.getLang("generator-place-permission"))) {
            plugin.sendMessage(player, "missing-place-permission", heldGenerator);
            event.setCancelled(true);
            return;
        }

        final Block blockPlaced = event.getBlockPlaced();
        final Location targetLocation = blockPlaced.getLocation();

        plugin.getScheduler().runTaskLater(1, () -> {
            blockPlaced.setType(Material.AIR, false);
            try {
                Generator.create(heldGenerator, targetLocation, GeneratorTools.getToolRotation(heldItem));
            } catch (GeneratorOverlapException e) {
                final ItemStack placedItem = heldItem.clone();
                placedItem.setAmount(1);
                PlayerUtil.giveItemsOrDrop(player, placedItem);
                PlayerUtil.sendActionBar(player, plugin.getLang("generator-overlap"));
                return;
            }
            PlayerUtil.sendActionBar(player, plugin.getLang("placed-generator"));
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
            plugin.getGenerators().removeGenerator(clickedGenerator);
            return;
        }

        final OrestackPlayer playerState = plugin.getPlayer(event.getPlayer().getUniqueId());
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
