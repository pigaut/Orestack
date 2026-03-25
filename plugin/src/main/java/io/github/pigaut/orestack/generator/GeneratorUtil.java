package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.data.structure.*;
import io.github.pigaut.voxel.data.structure.Structure;
import io.github.pigaut.voxel.util.Server;
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

        GeneratorPhase generatorPhase = generator.getPhase();
        if (generatorPhase.getDecorativeBlocks().contains(block.getType())) {
            return;
        }

        GeneratorMineEvent generatorMineEvent = new GeneratorMineEvent(player, block, generator.getOrigin(), generator.getName(), generator.getState().getCurrentPhase());
        {
            if (generatorPhase.isIdle()) {
                generatorMineEvent.setIdle(true);
            }

            if (generatorPhase.isDropItems()) {
                ItemStack tool = player.getInventory().getItemInMainHand();
                generatorMineEvent.setItemDrops(block.getDrops(tool));
            }

            if (generatorPhase.isDropExp()) {
                generatorMineEvent.setExpDrops(expToDrop);
            }

            int toolDamage = generatorPhase.getToolDamage().intValue();
            generatorMineEvent.setToolDamage(toolDamage);

            Server.callEvent(generatorMineEvent);
        }

        if (!generatorMineEvent.isCancelled()) {
            Function breakFunction = generatorPhase.getBreakFunction();
            if (breakFunction != null) {
                Context context = Context.builder()
                        .withPlayer(player)
                        .withPlayerState(plugin.getPlayerState(player))
                        .withTool(player.getInventory().getItemInMainHand())
                        .withBlock(block)
                        .with(Generator.class, generator)
                        .withEvent(generatorMineEvent)
                        .build();

                breakFunction.run(context);
            }
        }

        if (!generatorMineEvent.isCancelled() && generatorPhase.getState().isHarvestable()) {
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

    public static void init(@NotNull Generator generator, int phaseIndex) {
        setPhase(generator, phaseIndex, true, true);
    }

    public static void grow(@NotNull Generator generator, int growthAmount) {
        if (generator.isFullyGrown()) {
            return;
        }

        GeneratorPhase currentPhase = generator.getPhase();
        if (currentPhase.getGrowthTime() == 0) {
            return;
        }

        Integer nextPhaseIndex = generator.getNextGrowthPhase(growthAmount);
        if (nextPhaseIndex == null) {
            return;
        }

        // Call generator growth event
        GeneratorGrowthEvent growthEvent = new GeneratorGrowthEvent(generator.getOrigin(), generator.getName(), generator.getState().getCurrentPhase());
        Server.callEvent(growthEvent);
        if (growthEvent.isCancelled()) {
            return;
        }

        setPhase(generator, nextPhaseIndex, false, true);
    }

    public static void harvest(@NotNull Generator generator) {
        if (generator.isDepleted()) {
            return;
        }

        int nextPhaseIndex = generator.getNextHarvestingPhase();
        setPhase(generator, nextPhaseIndex, false, false);
    }

    public static void setPhase(@NotNull Generator generator, int phaseIndex) {
        setPhase(generator, phaseIndex, false, true);
    }

    private static void setPhase(@NotNull Generator generator, int phaseIndex, boolean init, boolean growing) {
        if (phaseIndex < 0 || phaseIndex > generator.getMaxPhase()) {
            return;
        }

        GeneratorPhase newPhase = generator.getPhase(phaseIndex);
        GeneratorState state = generator.getState();
        Location origin = generator.getOrigin();
        Rotation rotation = generator.getRotation();
        Context context = Context.builder()
                .withBlock(origin.getBlock())
                .with(Generator.class, generator)
                .build();

        // Replace phase
        state.setCurrentPhase(phaseIndex);

        // Replace structure
        Structure existingStructure = state.getStructure();
        StructureTemplate newStructure = newPhase.getStructureTemplate();
        if (init) {
            state.setStructure(newStructure.place(origin, rotation));
        }
        else {
            state.setStructure(existingStructure.replace(newStructure));
        }

        // Replace health
        Double newHealth = newPhase.getMaxHealth();
        state.setHealth(newHealth);

        // Replace hologram
        state.removeHologram();
        HologramTemplate newHologram = newPhase.getHologramTemplate();
        if (newHologram != null) {
            Location offsetLocation = origin.clone().add(0.5, 0.5, 0.5);
            state.setHologram(newHologram.spawn(offsetLocation, rotation, context));
        }

        // Replace growth task
        state.cancelGrowthTask();
        if (newPhase.getState() != GrowthState.REGROWN) {
            int growthTime = newPhase.getGrowthTime();
            state.setGrowthStart(Instant.now());
            state.setGrowthTask(plugin.getScheduler().runTaskLater(growthTime, () -> {
                state.setGrowthTask(null);
                grow(generator, 1);
            }));
        }

        // Run custom functions
        if (growing) {
            Function growthFunction = newPhase.getGrowthFunction();
            if (growthFunction != null) {
                growthFunction.run(context);
            }
        }
    }

    public static void damage(@NotNull Generator generator, @NotNull Player player, @NotNull Context context, double damage) {
        GeneratorState state = generator.getState();
        Double health = state.getPhaseHealth();
        if (health == null) {
            return;
        }

        double newHealth = Math.max(0, health - damage);
        if (newHealth > 0) {
            state.setHealth(newHealth);
            return;
        }

        GeneratorDestroyEvent event = new GeneratorDestroyEvent(player, generator.getOrigin(), generator.getName(), generator.getState().getCurrentPhase());
        Server.callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        Function onDestroy = generator.getPhase().getDestroyFunction();
        if (onDestroy != null) {
            onDestroy.run(context);
        }

        harvest(generator);
    }

}


