package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.damage.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.voxel.server.Server;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorBlock {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    private GeneratorBlock() {}

    public static void mineBlock(@NotNull Generator generator, @NotNull Player player, @NotNull Block block, int expToDrop) {
        if (generator.isUpdating()) {
            return;
        }

        if (!generator.matchBlocks()) {
            plugin.getGenerators().unregisterGenerator(generator);
            return;
        }

        GeneratorStage generatorStage = generator.getCurrentStage();
        if (generatorStage.getDecorativeBlocks().contains(block.getType())) {
            return;
        }

        OrestackPlayer playerState = plugin.getPlayerState(player);
        playerState.updatePlaceholders(generator);

        GeneratorMineEvent generatorMineEvent = new GeneratorMineEvent(player, block);
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

            int toolDamage = generatorStage.getToolDamage().getInteger();
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
                ItemUtil.dropItems(dropLocation, itemDrops);
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
                generator.previousStage();
            }
        }
    }

}
