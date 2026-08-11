package io.github.pigaut.rpg.command.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.bukkit.*;


import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.generator.tool.*;
import io.github.pigaut.rpg.module.mob.spawnegg.*;
import org.jetbrains.annotations.*;

public class GeneratorGetSubCommand extends SubCommand {

    public GeneratorGetSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "get");
        withPermission(plugin.getPermission("generator.get"));
        withDescription(plugin.getTranslation("generator-get-command"));
        withParameter(OrestackParameters.GENERATOR_NAME);
        withPlayerExecution((player, context, args) -> {
            Tool tool = plugin.getTool("generator");
            if (!(tool instanceof GeneratorTool generatorTool)) {
                plugin.sendMessage(player, context, "tool-not-found");
                return;
            }

            GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, context, "generator-not-found");
                return;
            }

            PlayerUtil.giveItemsOrDrop(player, generatorTool.createItem(generator));
            plugin.sendMessage(player, context, "received-generator");
        });
    }

}
