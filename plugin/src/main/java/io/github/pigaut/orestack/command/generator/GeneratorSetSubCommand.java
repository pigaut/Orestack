package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.bukkit.Rotation;


import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.util.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorSetSubCommand extends SubCommand {

    public GeneratorSetSubCommand(@NotNull OrestackPlugin plugin) {
        super("set", plugin);
        withPermission(plugin.getPermission("generator.set"));
        withDescription(plugin.getTranslation("generator-set-command"));
        withParameter(GeneratorParameters.GENERATOR_NAME);
        withPlayerExecution((player, context, args) -> {
            GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, context, "generator-not-found");
                return;
            }

            Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, context, "too-far-away");
                return;
            }

            Location location = targetBlock.getLocation();

            GeneratorPlaceEvent generatorPlaceEvent = new GeneratorPlaceEvent(player, location, generator.getName(), generator.getOccupiedBlocks(location, Rotation.NONE));
            Server.callEvent(generatorPlaceEvent);

            if (generatorPlaceEvent.isCancelled()) {
                plugin.sendMessage(player, context, "generator-conflict");
                return;
            }

            try {
                Generator.create(generator, location);
                plugin.sendMessage(player, context, "created-generator");
            }
            catch (GeneratorOverlapException e) {
                plugin.sendMessage(player, context, "generator-overlap");
            } catch (GeneratorLimitException e) {
                plugin.sendMessage(player, context, "large-generator-limit");
            }
        });
    }

}
