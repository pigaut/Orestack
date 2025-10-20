package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.core.function.*;
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

        final GeneratorTemplate heldGenerator = GeneratorTool.getGeneratorFromTool(heldItem);
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
            GeneratorTool.incrementToolRotation(heldItem);
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
        final String generatorName = GeneratorTool.getGeneratorNameFromTool(heldItem);
        if (generatorName == null) {
            return;
        }

        event.setCancelled(true);

        final Player player = event.getPlayer();
        final GeneratorTemplate heldGenerator = plugin.getGeneratorTemplate(generatorName);
        if (heldGenerator == null) {
            plugin.sendMessage(player, "generator-not-exists");
            return;
        }

        if (!player.hasPermission("orestack.generator.place")) {
            plugin.sendMessage(player, "cannot-place-generator", heldGenerator);
            return;
        }

        final Block blockPlaced = event.getBlockPlaced();
        final Location targetLocation = blockPlaced.getLocation();

        plugin.getScheduler().runTaskLater(1, () -> {
            blockPlaced.setType(Material.AIR, false);
            try {
                Generator.create(heldGenerator, targetLocation, GeneratorTool.getToolRotation(heldItem));
                PlayerUtil.sendActionBar(player, plugin.getLang("placed-generator"));
            }
            catch (GeneratorOverlapException e) {
                PlayerUtil.sendActionBar(player, plugin.getLang("generator-overlap"));
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

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
        }

        if (!clickedGenerator.matchBlocks()) {
            plugin.getGenerators().unregisterGenerator(clickedGenerator);
            return;
        }

        Block block = event.getClickedBlock();
        GeneratorStage stage = clickedGenerator.getCurrentStage();
        if (stage.getDecorativeBlocks().contains(block.getType())) {
            return;
        }

        OrestackPlayer playerState = plugin.getPlayerState(event.getPlayer());
        if (!playerState.hasFlag("orestack:click_cooldown")) {
            playerState.addTemporaryFlag("orestack:click_cooldown", stage.getClickCooldown());

            GeneratorInteractEvent generatorInteractEvent = new GeneratorInteractEvent(playerState, clickedGenerator);
            SpigotServer.callEvent(generatorInteractEvent);
            if (!generatorInteractEvent.isCancelled()) {
                playerState.updatePlaceholders(clickedGenerator);
                Function clickFunction = stage.getClickFunction();
                if (clickFunction != null) {
                    clickFunction.run(playerState, event, block);
                }
            }
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && !playerState.hasFlag("orestack:hit_cooldown")) {
            playerState.addTemporaryFlag("orestack:hit_cooldown", stage.getHitCooldown());

            GeneratorHitEvent generatorHitEvent = new GeneratorHitEvent(playerState, clickedGenerator);
            SpigotServer.callEvent(generatorHitEvent);
            if (!generatorHitEvent.isCancelled()) {
                playerState.updatePlaceholders(clickedGenerator);
                Function hitFunction = stage.getHitFunction();
                if (hitFunction != null) {
                    hitFunction.run(playerState, event, block);
                }
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && !playerState.hasFlag("orestack:harvest_cooldown")) {
            playerState.addTemporaryFlag("orestack:harvest_cooldown", stage.getHarvestCooldown());

            GeneratorHarvestEvent generatorHarvestEvent = new GeneratorHarvestEvent(playerState, clickedGenerator);
            SpigotServer.callEvent(generatorHarvestEvent);
            if (!generatorHarvestEvent.isCancelled()) {
                playerState.updatePlaceholders(clickedGenerator);
                Function harvestFunction = stage.getHarvestFunction();
                if (harvestFunction != null) {
                    harvestFunction.run(playerState, event, block);
                }
            }
        }

    }

}
