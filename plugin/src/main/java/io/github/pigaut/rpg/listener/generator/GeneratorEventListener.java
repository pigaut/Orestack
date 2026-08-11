package io.github.pigaut.rpg.listener.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.hook.veinminer.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.exception.*;
import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.instanced.*;
import io.github.pigaut.rpg.module.generator.phase.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.module.generator.tool.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.settings.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.server.Server;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class GeneratorEventListener implements Listener {

    private final RpgMakerPlugin plugin;

    public GeneratorEventListener(RpgMakerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (VirtualGenerator virtualGenerator : plugin.getGenerators().getAllVirtual()) {
            if (!virtualGenerator.isViewer(player)) {
                virtualGenerator.addViewer(player);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (VirtualGenerator virtualGenerator : plugin.getGenerators().getAllVirtual()) {
            virtualGenerator.removeViewer(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleGeneratorBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Generator generator = plugin.getGenerator(player, block.getLocation());
        if (generator == null) {
            return;
        }

        event.setCancelled(true);
        if (generator instanceof InstancedGenerator) {
            return;
        }

        GeneratorPhase generatorPhase = generator.getPhase();
        if (generatorPhase.getDecorativeBlocks().contains(block.getType())) {
            return;
        }

        int expToDrop = event.getExpToDrop();

        RpgSettings settings = plugin.getSettings();
        if (settings.isVeinMiner()) {
            int maxVeinSize = settings.getToolMaxVeinSize(player.getInventory().getItemInMainHand());
            if (maxVeinSize > 1) {
                GeneratorBlockVein.mineBlocks(generator, player, maxVeinSize, expToDrop);
                return;
            }
        }

        generator.mineBlock(player, block, expToDrop);
    }

    @EventHandler
    public void handleGeneratorBlockInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.hasItem() && plugin.isTool(event.getItem())) {
            return;
        }

        Player player = event.getPlayer();
        Generator generator = plugin.getGenerator(player, event.getClickedBlock().getLocation());
        if (generator == null) {
            return;
        }

        if (!generator.isValid()) {
            generator.remove();
            return;
        }

        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            if (player.hasPermission("rpg-maker.generator.build-on") && event.hasItem()
                    && !MaterialUtil.isInteractable(clickedBlock.getType())) {
                event.setCancelled(false);
            }
        }

        GeneratorPhase phase = generator.getPhase();
        if (phase.getDecorativeBlocks().contains(clickedBlock.getType())) {
            return;
        }

        RpgPlayerState playerState = plugin.getPlayerState(player);
        Context context = Context.builder(plugin)
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

}
