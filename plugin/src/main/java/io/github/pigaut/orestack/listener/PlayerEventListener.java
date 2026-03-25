package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.hook.veinminer.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.util.Server;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class PlayerEventListener implements Listener {

    private final OrestackPlugin plugin;

    public PlayerEventListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleGeneratorBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Generator generator = plugin.getGenerator(block.getLocation());
        if (generator == null) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        int expToDrop = event.getExpToDrop();

        OrestackSettings settings = plugin.getSettings();
        if (settings.isVeinMiner()) {
            int maxVeinSize = settings.getToolMaxVeinSize(player.getInventory().getItemInMainHand());
            if (maxVeinSize > 1) {
                GeneratorBlockVein.mineBlocks(generator, player, maxVeinSize, expToDrop);
                return;
            }
        }

        GeneratorUtil.mineBlock(generator, player, block, expToDrop);
    }

    @EventHandler
    public void handleGeneratorBlockInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.hasItem() && GeneratorTool.isValidItem(event.getItem())) {
            return;
        }

        Generator generator = plugin.getGenerator(event.getClickedBlock().getLocation());
        if (generator == null) {
            return;
        }

        if (!generator.isValid()) {
            generator.remove();
            return;
        }

        Player player = event.getPlayer();
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            if (player.hasPermission("orestack.build.on.generator") && event.hasItem()
                    && !MaterialUtil.isInteractable(clickedBlock.getType())) {
                event.setCancelled(false);
            }
        }

        GeneratorPhase phase = generator.getPhase();
        if (phase.getDecorativeBlocks().contains(clickedBlock.getType())) {
            return;
        }

        OrestackPlayer playerState = plugin.getPlayerState(player);
        Context context = Context.builder()
                .withPlayer(player)
                .withPlayerState(playerState)
                .withAction(action)
                .withTool(player.getInventory().getItemInMainHand())
                .withBlock(clickedBlock)
                .with(Generator.class, generator)
                .withEvent(event)
                .build();

        if (playerState.hasFlag("orestack:click_cooldown")) {
            return;
        }

        GeneratorInteractEvent generatorInteractEvent = new GeneratorInteractEvent(player, action, clickedBlock, generator.getOrigin(), generator.getName(), generator.getState().getCurrentPhase());
        Server.callEvent(generatorInteractEvent);

        if (generatorInteractEvent.isCancelled()) {
            return;
        }

        playerState.addTemporaryFlag("orestack:click_cooldown", phase.getClickCooldown());
        Function clickFunction = phase.getClickFunction();
        if (clickFunction != null) {
            clickFunction.run(context.withEvent(generatorInteractEvent));
        }

        if (action == Action.LEFT_CLICK_BLOCK) {
            if (playerState.hasFlag("orestack:hit_cooldown")) {
                return;
            }

            GeneratorHitEvent generatorHitEvent = new GeneratorHitEvent(player, action, clickedBlock, generator.getOrigin(), generator.getName(), generator.getState().getCurrentPhase());
            Server.callEvent(generatorHitEvent);

            if (generatorHitEvent.isCancelled()) {
                return;
            }

            playerState.addTemporaryFlag("orestack:hit_cooldown", phase.getHitCooldown());
            Function hitFunction = phase.getHitFunction();
            if (hitFunction != null) {
                hitFunction.run(context.withEvent(generatorHitEvent));
            }
        }
        else if (action == Action.RIGHT_CLICK_BLOCK) {
            if (playerState.hasFlag("orestack:harvest_cooldown")) {
                return;
            }

            GeneratorHarvestEvent generatorHarvestEvent = new GeneratorHarvestEvent(player, action, clickedBlock, generator.getOrigin(), generator.getName(), generator.getState().getCurrentPhase());
            Server.callEvent(generatorHarvestEvent);

            if (generatorHarvestEvent.isCancelled()) {
                return;
            }

            playerState.addTemporaryFlag("orestack:harvest_cooldown", phase.getHarvestCooldown());
            Function harvestFunction = phase.getHarvestFunction();
            if (harvestFunction != null) {
                harvestFunction.run(context.withEvent(generatorHarvestEvent));
            }
        }
    }

    @EventHandler
    public void handleGeneratorItemInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        ItemStack heldItem = event.getItem();
        if (heldItem == null) {
            return;
        }

        GeneratorTemplate heldGenerator = GeneratorTool.getGeneratorTemplate(heldItem);
        if (heldGenerator == null) {
            return;
        }

        Player player = event.getPlayer();
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();

        Context context = Context.builder()
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withTool(heldItem)
                .withBlock(clickedBlock)
                .build();

        if (action == Action.LEFT_CLICK_AIR && player.isSneaking()) {
            event.setCancelled(true);

            if (!player.hasPermission("orestack.generator.rotate")) {
                plugin.sendMessage(player, context, "cannot-rotate-generator");
                return;
            }

            GeneratorTool.switchToolRotation(heldItem);
            PlayerUtil.sendActionBar(player, plugin.getTranslation("changed-generator-rotation"));
            return;
        }

        if (clickedBlock == null) {
            return;
        }

        if (action == Action.LEFT_CLICK_BLOCK) {
            Generator clickedGenerator = plugin.getGenerator(clickedBlock.getLocation());
            if (clickedGenerator == null || !clickedGenerator.getTemplate().equals(heldGenerator)) {
                return;
            }

            event.setCancelled(true);
            context = context.with(Generator.class, clickedGenerator);
            if (!player.hasPermission("orestack.generator.break")) {
                plugin.sendMessage(player, context, "cannot-break-generator");
                return;
            }

            clickedGenerator.remove();
            PlayerUtil.sendActionBar(player, plugin.getTranslation("broke-generator"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void handleGeneratorPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlockPlaced().getLocation();
        Context context = Context.builder()
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withTool(event.getItemInHand())
                .withBlock(event.getBlockPlaced())
                .build();

        if (plugin.getGenerators().isGenerator(location)) {
            plugin.sendMessage(player, context, "generator-occupied-block");
            event.setCancelled(true);
            return;
        }

        ItemStack heldItem = event.getItemInHand();
        if (!GeneratorTool.isValidItem(heldItem)) {
            return;
        }

        event.setCancelled(true);

        GeneratorTemplate generatorTemplate = GeneratorTool.getGeneratorTemplate(heldItem);
        if (generatorTemplate == null) {
            plugin.sendMessage(player, context, "generator-not-exists");
            return;
        }

        context = context.with(GeneratorTemplate.class, generatorTemplate);
        if (!player.hasPermission("orestack.generator.place")) {
            plugin.sendMessage(player, context, "cannot-place-generator");
            return;
        }

        Rotation rotation = GeneratorTool.getRotation(heldItem);
        if (rotation == null) {
            plugin.sendMessage(player, context, "corrupt-tool-rotation");
            return;
        }

        GeneratorPlaceEvent generatorPlaceEvent = new GeneratorPlaceEvent(player, location, generatorTemplate.getName(), generatorTemplate.getOccupiedBlocks(location, rotation));
        Server.callEvent(generatorPlaceEvent);

        if (generatorPlaceEvent.isCancelled()) {
            PlayerUtil.sendActionBar(player, plugin.getTranslation("generator-conflict"));
            return;
        }

        plugin.getRegionScheduler(location).runTaskLater(1, () -> {
            try {
                Generator.create(generatorTemplate, location, rotation);
                PlayerUtil.sendActionBar(player, plugin.getTranslation("placed-generator"));
            }
            catch (GeneratorOverlapException e) {
                PlayerUtil.sendActionBar(player, plugin.getTranslation("generator-overlap"));
            } catch (GeneratorLimitException e) {
                plugin.sendMessage(player, Context.EMPTY, "large-generator-limit");
            }
        });
    }

}
