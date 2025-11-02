package io.github.pigaut.orestack.generator;

import dev.lone.itemsadder.api.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import java.util.*;

public class GeneratorLogic {

    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    private GeneratorLogic() {}

    public static void breakBlock(Generator generator, Player player, Block block, int expToDrop) {
        if (generator.isUpdating()) {
            return;
        }

        if (!generator.matchBlocks()) {
            plugin.getGenerators().unregisterGenerator(generator);
            return;
        }

        GeneratorStage stage = generator.getCurrentStage();
        if (stage.getDecorativeBlocks().contains(block.getType())) {
            return;
        }

        OrestackPlayer playerState = plugin.getPlayerState(player);
        playerState.updatePlaceholders(generator);

        GeneratorMineEvent generatorMineEvent = new GeneratorMineEvent(player, block, stage.isIdle());
        SpigotServer.callEvent(generatorMineEvent);

        if (!generatorMineEvent.isCancelled()) {
            Function breakFunction = stage.getBreakFunction();
            if (breakFunction != null) {
                breakFunction.run(playerState, generatorMineEvent, block);
            }
        }

        if (generatorMineEvent.isCancelled() || !stage.getState().isHarvestable()) {
            return;
        }

        if (stage.isDropItems()) {
            ItemStack tool = player.getInventory().getItemInMainHand();
            Collection<ItemStack> drops = block.getDrops(tool);

            if (SpigotServer.isPluginEnabled("ItemsAdder")) {
                CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
                if (customBlock != null) {
                    drops = customBlock.getLoot(tool, true);
                }
            }

            Location location = block.getLocation().add(0.5, 1, 0.5);
            for (ItemStack itemDrop : drops) {
                ItemUtil.dropItem(location, itemDrop, itemDrop.getAmount());
            }
        }

        if (stage.isDropExp()) {
            Exp.drop(block.getLocation(), expToDrop);
        }

        if (!generatorMineEvent.isKeepStage()) {
            generator.previousStage();
        }
    }

}
