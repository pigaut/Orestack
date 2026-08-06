package io.github.pigaut.rpg.command.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.tools.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.bukkit.*;


import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tools.*;
import io.github.pigaut.rpg.module.generator.template.*;
import org.jetbrains.annotations.*;

public class GeneratorGetSubCommand extends SubCommand {

    public GeneratorGetSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "get");
        withPermission(plugin.getPermission("generator.get"));
        withDescription(plugin.getTranslation("generator-get-command"));
        withParameter(OrestackParameters.GENERATOR_NAME);
        withPlayerExecution((player, context, args) -> {
            GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, context, "generator-not-found");
                return;
            }

            PlayerUtil.giveItemsOrDrop(player, GeneratorTool.createItem(generator));
            plugin.sendMessage(player, context, "received-generator");
        });
    }

}
