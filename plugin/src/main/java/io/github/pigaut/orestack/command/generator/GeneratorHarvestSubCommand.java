package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

public class GeneratorHarvestSubCommand extends SubCommand {

    public GeneratorHarvestSubCommand(@NotNull OrestackPlugin plugin) {
        super("harvest-all", plugin);
        withPermission(plugin.getPermission("generator.harvest-all"));
        withDescription(plugin.getTranslation("generator-harvest-all-command"));
        withParameter(GeneratorParameters.GENERATOR_NAME);
        withPlayerExecution((player, args, placeholders) -> {
            GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "generator-not-found", placeholders);
                return;
            }
            for (Generator geneator : plugin.getGenerators().getAllGenerators()) {
                if (geneator.getTemplate() == generator) {
                    for (Block block : geneator.getOccupiedBlocks()) {
                        BlockBreakEvent event = new BlockBreakEvent(block, player);
                        Server.callEvent(event);
                        break;
                    }
                }
            }
            plugin.sendMessage(player, "harvested-all-generators", placeholders, generator);
        });
    }

}
