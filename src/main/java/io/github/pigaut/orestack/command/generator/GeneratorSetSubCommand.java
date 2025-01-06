package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorSetSubCommand extends SubCommand {

    public GeneratorSetSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("SET_GENERATOR_COMMAND", "set"), plugin);
        addParameter(plugin.getLang("GENERATOR_NAME_PARAMETER", "generator-name"));
        withDescription(plugin.getLang("SET_GENERATOR_DESCRIPTION"));

        withPermission("orestack.admin.set")
                .withPlayerExecution((player, args) -> {
                    final Generator generator = plugin.getGenerator(args[0]);

                    if (generator == null) {
                        player.sendMessage(plugin.getLang("GENERATOR_NOT_FOUND"));
                        return;
                    }

                    final Block targetBlock = player.getTargetBlockExact(10);
                    if (targetBlock == null) {
                        player.sendMessage(plugin.getLang("TOO_FAR_AWAY"));
                        return;
                    }

                    final Location location = targetBlock.getLocation();
                    plugin.getGenerators().addBlockGenerator(generator, location);
                    player.sendMessage(plugin.getLang("CREATED_GENERATOR"));
                });

        withPlayerCompletion((player, args) -> plugin.getGenerators().getGeneratorNames());

    }

}
