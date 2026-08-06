package io.github.pigaut.rpg.command.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.module.generator.exception.*;
import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.instanced.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.transform.Rotation;


import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.generator.exception.*;
import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.instanced.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.server.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorSetSubCommand extends SubCommand {

    public GeneratorSetSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "set");
        withPermission(plugin.getPermission("generator.set"));
        withDescription(plugin.getTranslation("generator-set-command"));
        withParameter(OrestackParameters.GENERATOR_NAME);
        withParameter(CommandParameter.create("generator-type", "global", (sender, args) -> List.of("global", "per-player")));
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

            String type = args[1];
            try {
                if (type.equalsIgnoreCase("global")) {
                    GlobalGenerator.create(generator, location);
                }
                else if (type.equalsIgnoreCase("per-player")) {
                    VirtualGenerator.create(generator, location);
                }
                else {
                    plugin.sendMessage(player, context, "unknown-generator-type");
                    return;
                }
                plugin.sendMessage(player, context, "created-generator");
            }
            catch (GeneratorCreateException e) {
                PlayerUtil.sendChat(player, e.getMessage());
            }
        });
    }

}
