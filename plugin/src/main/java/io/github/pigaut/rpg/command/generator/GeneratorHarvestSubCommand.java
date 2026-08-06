package io.github.pigaut.rpg.command.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;

import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.server.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

public class GeneratorHarvestSubCommand extends SubCommand {

    public GeneratorHarvestSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "harvest-all");
        withPermission(plugin.getPermission("generator.harvest-all"));
        withDescription(plugin.getTranslation("generator-harvest-all-command"));
        withParameter(OrestackParameters.GENERATOR_NAME);
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
