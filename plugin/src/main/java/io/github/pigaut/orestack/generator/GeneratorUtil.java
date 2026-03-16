package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.generator.stage.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.core.structure.Structure;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.server.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.time.*;
import java.util.*;

public class GeneratorUtil {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    public static void mineBlock(@NotNull Generator generator, @NotNull Player player, @NotNull Block block, int expToDrop) {
        if (!generator.isValid()) {
            generator.remove();
            return;
        }

        GeneratorStage generatorStage = generator.getStage();
        if (generatorStage.getDecorativeBlocks().contains(block.getType())) {
            return;
        }

        OrestackPlayer playerState = plugin.getPlayerState(player);
        playerState.updatePlaceholders(generator.getState());

        GeneratorMineEvent generatorMineEvent = new GeneratorMineEvent(generator.getName(), generator.getState().getCurrentStage(), player, block);
        {
            if (generatorStage.isIdle()) {
                generatorMineEvent.setIdle(true);
            }

            if (generatorStage.isDropItems()) {
                ItemStack tool = player.getInventory().getItemInMainHand();
                generatorMineEvent.setItemDrops(block.getDrops(tool));
            }

            if (generatorStage.isDropExp()) {
                generatorMineEvent.setExpDrops(expToDrop);
            }

            int toolDamage = generatorStage.getToolDamage().intValue();
            generatorMineEvent.setToolDamage(toolDamage);

            Server.callEvent(generatorMineEvent);
        }

        if (!generatorMineEvent.isCancelled()) {
            Function breakFunction = generatorStage.getBreakFunction();
            if (breakFunction != null) {
                breakFunction.run(playerState, generatorMineEvent, block);
            }
        }

        if (!generatorMineEvent.isCancelled() && generatorStage.getState().isHarvestable()) {
            Location dropLocation = block.getLocation().add(0.5, 1, 0.5);

            Collection<ItemStack> itemDrops = generatorMineEvent.getItemDrops();
            if (itemDrops != null) {
                for (ItemStack itemDrop : itemDrops) {
                    ItemUtil.dropItem(dropLocation, itemDrop);
                }
            }

            int expDrops = generatorMineEvent.getExpDrops();
            if (expDrops != 0) {
                Exp.drop(dropLocation, expDrops);
            }

            int toolDamage = generatorMineEvent.getToolDamage();
            if (toolDamage != 0) {
                ItemUtil.damagePlayerTool(player, toolDamage);
            }

            if (!generatorMineEvent.isIdle()) {
                harvest(generator);
            }
        }
    }

    public static void init(@NotNull Generator generator, int stageIndex) {
        setStage(generator, stageIndex, true, true);
    }

    public static void grow(@NotNull Generator generator, int growthAmount) {
        if (generator.isFullyGrown()) {
            return;
        }

        GeneratorStage currentStage = generator.getStage();
        if (currentStage.getGrowthTime() == 0) {
            return;
        }

        Integer nextStageIndex = generator.getNextGrowthStage(growthAmount);
        if (nextStageIndex == null) {
            return;
        }

        // Call generator growth event
        GeneratorGrowthEvent growthEvent = new GeneratorGrowthEvent(generator.getName(), generator.getState().getCurrentStage(), generator.getOrigin());
        Server.callEvent(growthEvent);
        if (growthEvent.isCancelled()) {
            return;
        }

        setStage(generator, nextStageIndex, false, true);
    }

    public static void harvest(@NotNull Generator generator) {
        if (generator.isDepleted()) {
            return;
        }

        int nextStageIndex = generator.getNextHarvestingStage();
        setStage(generator, nextStageIndex, false, false);
    }

    public static void setStage(@NotNull Generator generator, int stageIndex) {
        setStage(generator, stageIndex, false, true);
    }

    private static void setStage(@NotNull Generator generator, int stageIndex, boolean init, boolean growing) {
        if (stageIndex < 0 || stageIndex > generator.getMaxStage()) {
            return;
        }

        GeneratorStage newStage = generator.getStage(stageIndex);
        GeneratorState state = generator.getState();
        Location origin = generator.getOrigin();
        Rotation rotation = generator.getRotation();

        // Replace stage
        state.setCurrentStage(stageIndex);

        // Replace structure
        Structure existingStructure = state.getStructure();
        StructureTemplate newStructure = newStage.getStructureTemplate();
        if (init) {
            state.setStructure(newStructure.place(origin, rotation));
        }
        else {
            state.setStructure(existingStructure.replace(newStructure));
        }

        // Replace hologram
        state.removeHologram();
        Hologram newHologram = newStage.getHologram();
        if (newHologram != null) {
            Location offsetLocation = origin.clone().add(0.5, 0.5, 0.5);
            state.setHologram(newHologram.spawn(offsetLocation, rotation, List.of(state)));
        }

        // Replace health
        Double newHealth = newStage.getHealth();
        state.setHealth(newHealth);

        // Replace growth task
        state.cancelGrowthTask();
        if (newStage.getState() != GrowthState.REGROWN) {
            int growthTime = newStage.getGrowthTime();
            state.setGrowthStart(Instant.now());
            state.setGrowthTask(plugin.getScheduler().runTaskLater(growthTime, () -> {
                state.setGrowthTask(null);
                grow(generator, 1);
            }));
        }

        // Run custom functions
        if (growing) {
            Function growthFunction = newStage.getGrowthFunction();
            if (growthFunction != null) {
                Block block = origin.getBlock();
                growthFunction.run(block);
            }
        }
    }

    public static void damage(@NotNull Generator generator, @NotNull PlayerState damageDealer, double damage) {
        GeneratorState state = generator.getState();
        Double health = state.getHealth();
        if (health == null) {
            return;
        }

        double newHealth = Math.max(0, health - damage);
        if (newHealth > 0) {
            state.setHealth(newHealth);
            return;
        }

        GeneratorDestroyEvent event = new GeneratorDestroyEvent(generator.getName(), generator.getState().getCurrentStage(), damageDealer.asPlayer());
        Server.callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        Function onDestroy = generator.getStage().getDestroyFunction();
        if (onDestroy != null) {
            onDestroy.dispatch(damageDealer, event, generator.getBlock(), null);
        }

        harvest(generator);
    }

}


