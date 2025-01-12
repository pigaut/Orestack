package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class GeneratorGetSubCommand extends SubCommand {

    public GeneratorGetSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("GET_GENERATOR_COMMAND", "get"), plugin);
        addParameter(plugin.getLang("GENERATOR_NAME_PARAMETER", "generator-name"));
        withDescription(plugin.getLang("GET_GENERATOR_DESCRIPTION"));
        withPermission("orestack.generator.get");
        withPlayerExecution((player, args) -> {
            final Generator generator = plugin.getGenerator(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "GENERATOR_NOT_FOUND", this);
                return;
            }
            PlayerUtil.giveItemsOrDrop(player, generator.getItem());
            plugin.sendMessage(player, "RECEIVED_GENERATOR", this, generator);
        });
        withPlayerCompletion((player, args) -> plugin.getGenerators().getGeneratorNames());
    }

}
