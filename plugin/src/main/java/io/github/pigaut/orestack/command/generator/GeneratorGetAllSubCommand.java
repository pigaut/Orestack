package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.core.tools.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.command.node.*;
import org.jetbrains.annotations.*;

public class GeneratorGetAllSubCommand extends SubCommand {

    public GeneratorGetAllSubCommand(@NotNull OrestackPlugin plugin) {
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
