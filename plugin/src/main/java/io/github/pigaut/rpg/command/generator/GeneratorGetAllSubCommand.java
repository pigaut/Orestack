package io.github.pigaut.rpg.command.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.tools.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tools.*;
import io.github.pigaut.rpg.module.generator.template.*;
import org.jetbrains.annotations.*;

public class GeneratorGetAllSubCommand extends SubCommand {

    public GeneratorGetAllSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "get-all");
        withPermission(plugin.getPermission("generator.get-all"));
        withDescription(plugin.getTranslation("generator-get-all-command"));
        withPlayerExecution((player, context, args) -> {
            for (GeneratorTemplate generator : plugin.getGeneratorTemplates().getAll()) {
                PlayerUtil.giveItemsOrDrop(player, GeneratorTool.createItem(generator));
            }
            plugin.sendMessage(player, context, "received-all-generators");
        });
    }
}
