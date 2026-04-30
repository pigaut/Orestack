package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.util.Server;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorUtil {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    public static void createGenerator(String worldId, int x, int y, int z, String generatorName, String rotationData, int phase) throws GeneratorCreateException {
        World world = Bukkit.getWorld(UUID.fromString(worldId));
        if (world == null) {
            throw new GeneratorCreateException(worldId, x, y, z, "world not found");
        }

        String worldName = world.getName();
        GeneratorTemplate template = plugin.getGeneratorTemplate(generatorName);
        if (template == null) {
            throw new GeneratorCreateException(worldName, x, y, z, "generator template not found");
        }

        Location origin = new Location(world, x, y, z);
        Rotation rotation = ParseUtil.parseEnumOrNull(Rotation.class, rotationData);
        if (rotation == null) {
            rotation = Rotation.NONE;
            plugin.getColoredLogger().warning(String.format("Failed to load rotation of generator at %s, %d, %d, %d. Default rotation (NONE) has been applied.",
                    worldName, x, y, z));
        }

        int maxPhase = template.getMaxPhase();
        if (phase > maxPhase) {
            plugin.getColoredLogger().warning(String.format("Failed to load phase of generator at %s, %d, %d, %d. Maximum phase (" + maxPhase + ") has been applied.",
                    worldName, x, y, z));
        }

        for (Block block : template.getOccupiedBlocks(origin, rotation)) {
            if (plugin.getGenerators().isGenerator(block.getLocation())) {
                throw new GeneratorOverlapException(world.getName(), x, y, z);
            }
        }

        int finalPhase = Math.min(phase, template.getMaxPhase());
        Rotation finalRotation = rotation;
        plugin.getScheduler().runTask(() -> {
            try {
                GlobalGenerator.create(template, origin, finalRotation, finalPhase);
            } catch (GeneratorCreateException ignored) {
                //Block overlaps are checked before scheduling
            }
        });
    }

    public static void mineBlock(@NotNull GlobalGenerator generator, @NotNull Player player, @NotNull Block block, int expToDrop) {
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
                        .with(GlobalGenerator.class, generator)
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
                generator.harvest();
            }
        }
    }

}


