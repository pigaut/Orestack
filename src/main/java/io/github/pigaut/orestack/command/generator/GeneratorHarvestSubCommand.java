package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.generator.parameter.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

public class GeneratorHarvestSubCommand extends LangSubCommand {

    public GeneratorHarvestSubCommand(@NotNull OrestackPlugin plugin) {
        super("harvest-all-generators", plugin);
        addParameter(new GeneratorNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "generator-not-found", placeholders);
                return;
            }
            for (Generator blockGenerator : plugin.getGenerators().getAllGenerators()) {
                if (blockGenerator.getTemplate() == generator) {
                    final Block block = blockGenerator.getBlocks().get(0);
                    final BlockBreakEvent event = new BlockBreakEvent(block, player);
                    SpigotServer.callEvent(event);
                }
            }
            plugin.sendMessage(player, "harvested-all-generators", placeholders, generator);
        });
    }

}
