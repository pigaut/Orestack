package io.github.pigaut.rpg.module.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.module.generator.phase.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.generator.phase.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.server.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.Collection;

public class GeneratorUtil {

    private final static RpgMakerPlugin plugin = RpgMakerPlugin.getInstance();

    public static void callGeneratorMineEvent(@NotNull Generator generator, int currentPhase, @NotNull Player player, @NotNull Block block, int expToDrop) {
        GeneratorPhase generatorPhase = generator.getPhase(currentPhase);
        GeneratorMineEvent event = new GeneratorMineEvent(player, block, generator.getOrigin(), generator.getName(), currentPhase);
        {
            if (generatorPhase.isIdle()) {
                event.setIdle(true);
            }

            if (generatorPhase.isDefaultItemDrops()) {
                event.setItemDrops(block.getDrops());
            }

            if (generatorPhase.isDefaultExpDrops()) {
                event.setExpDrops(expToDrop);
            }

            int toolDamage = generatorPhase.getToolDamage().intValue();
            event.setToolDamage(toolDamage);
        }

        Server.callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        ItemStack tool = player.getInventory().getItemInMainHand();
        Context context = Context.builder(plugin)
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withTool(tool)
                .withBlock(block)
                .with(Generator.class, generator)
                .withEvent(event)
                .build();

        // Run tool function
        ItemTemplate itemTemplate = plugin.getItemTemplate(tool);
        if (itemTemplate != null) {
            Function onBlockBreak = itemTemplate.getOnBlockBreak();
            if (onBlockBreak != null) {
                onBlockBreak.run(context);
                if (event.isCancelled()) {
                    return;
                }
            }
        }

        // Run generator function
        Function onBreak = generatorPhase.getOnBreak();
        if (onBreak != null) {
            onBreak.run(context);
            if (event.isCancelled()) {
                return;
            }
        }

        if (generatorPhase.getState().isHarvestable()) {
            Location dropLocation = block.getLocation().add(0.5, 1, 0.5);

            Collection<ItemStack> itemDrops = event.getItemDrops();
            if (itemDrops != null) {
                for (ItemStack itemDrop : itemDrops) {
                    BlockItemDrop blockItemDrop = ItemDrop.fromBlock(plugin, block, itemDrop);
                    blockItemDrop.spawn(context, ItemSpawnReason.GENERATOR_DROPS);
                }
            }

            int expDrops = event.getExpDrops();
            if (expDrops != 0) {
                ExpUtil.dropExp(dropLocation, expDrops);
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
