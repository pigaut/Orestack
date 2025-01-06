package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import org.jetbrains.annotations.*;

public class GeneratorGetSubCommand extends SubCommand {

    public GeneratorGetSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("GET_GENERATOR_COMMAND", "get"), plugin);
        addParameter(plugin.getLang("GENERATOR_NAME_PARAMETER", "generator-name"));
        withDescription(plugin.getLang("GET_GENERATOR_DESCRIPTION"));
        withPermission("orestack.generator.get");
        withPlayerStateExecution((player, args) -> {
            final Generator generator = plugin.getGenerator(args[0]);
            if (generator == null) {
                player.sendMessage(plugin.getLang("GENERATOR_NOT_FOUND"));
                return;
            }
            player.getInventory().addItem(generator.getItem());
            player.sendMessage(plugin.getLang("RECEIVED_GENERATOR"));
        });
        withPlayerCompletion((player, args) -> plugin.getGenerators().getGeneratorNames());
    }

}
