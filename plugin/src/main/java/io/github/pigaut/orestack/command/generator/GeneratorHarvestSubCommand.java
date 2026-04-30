package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.orestack.generator.template.*;

import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

public class GeneratorHarvestSubCommand extends SubCommand {

    public GeneratorHarvestSubCommand(@NotNull OrestackPlugin plugin) {
        super("harvest-all", plugin);
        withPermission(plugin.getPermission("generator.harvest-all"));
        withDescription(plugin.getTranslation("generator-harvest-all-command"));
        withParameter(GeneratorParameters.GENERATOR_NAME);
        withPlayerExecution((player, context, args) -> {
            GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, context, "generator-not-found");
                return;
            }
            for (GlobalGenerator geneator : plugin.getGenerators().getAllGlobal()) {
                if (geneator.getTemplate() == generator) {
                    for (Block block : geneator.getOccupiedBlocks()) {
                        BlockBreakEvent event = new BlockBreakEvent(block, player);
                        Server.callEvent(event);
                        break;
                    }
                }
            }
            plugin.sendMessage(player, context, "harvested-all-generators");
        });
    }

}
