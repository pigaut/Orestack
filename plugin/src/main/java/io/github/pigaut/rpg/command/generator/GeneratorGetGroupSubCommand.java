package io.github.pigaut.rpg.command.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.generator.tool.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorGetGroupSubCommand extends SubCommand {

    public GeneratorGetGroupSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "get-group");
        withPermission(plugin.getPermission("generator.get-group"));
        withDescription(plugin.getTranslation("generator-get-group-command"));
        withParameter(OrestackParameters.GENERATOR_GROUP);
        withPlayerExecution((player, context, args) -> {
            Tool tool = plugin.getTool("generator");
            if (!(tool instanceof GeneratorTool generatorTool)) {
                plugin.sendMessage(player, context, "tool-not-found");
                return;
            }

            List<GeneratorTemplate> groupGenerators = plugin.getGeneratorTemplates().getAll(args[0]);
            if (groupGenerators.isEmpty()) {
                plugin.sendMessage(player, context, "generator-group-not-found");
                return;
            }

            for (GeneratorTemplate generator : groupGenerators) {
                PlayerUtil.giveItemsOrDrop(player, generatorTool.createItem(generator));
            }
            plugin.sendMessage(player, context, "received-generator-group");
        });
    }

}
