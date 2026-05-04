package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.voxel.util.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorUtil {

    private final static OrestackPlugin plugin = OrestackPlugin.getInstance();

    public static void callGeneratorMineEvent(@NotNull Generator generator, int currentPhase, @NotNull Player player, @NotNull Block block, int expToDrop) {
        GeneratorPhase generatorPhase = generator.getPhase(currentPhase);
        GeneratorMineEvent event = new GeneratorMineEvent(player, block, generator.getOrigin(), generator.getName(), currentPhase);
        {
            if (generatorPhase.isIdle()) {
                event.setIdle(true);
            }

            if (generatorPhase.isDropItems()) {
                ItemStack tool = player.getInventory().getItemInMainHand();
                event.setItemDrops(block.getDrops(tool));
            }

            if (generatorPhase.isDropExp()) {
                event.setExpDrops(expToDrop);
            }

            int toolDamage = generatorPhase.getToolDamage().intValue();
            event.setToolDamage(toolDamage);
        }

        Server.callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        Function breakFunction = generatorPhase.getBreakFunction();
        if (breakFunction != null) {
            Context context = Context.builder()
                    .withPlayer(player)
                    .withPlayerState(plugin.getPlayerState(player))
                    .withTool(player.getInventory().getItemInMainHand())
                    .withBlock(block)
                    .with(Generator.class, generator)
                    .withEvent(event)
                    .build();

            breakFunction.run(context);
        }

        if (generatorPhase.getState().isHarvestable()) {
            Location dropLocation = block.getLocation().add(0.5, 1, 0.5);

            Collection<ItemStack> itemDrops = event.getItemDrops();
            if (itemDrops != null) {
                for (ItemStack itemDrop : itemDrops) {
                    ItemUtil.dropItem(dropLocation, itemDrop);
                }
            }

            int expDrops = event.getExpDrops();
            if (expDrops != 0) {
                Exp.drop(dropLocation, expDrops);
            }

            int toolDamage = event.getToolDamage();
            if (toolDamage != 0) {
                ItemUtil.damagePlayerTool(player, toolDamage);
            }

            if (!event.isIdle()) {
                generator.harvest();
            }
        }
    }

}
