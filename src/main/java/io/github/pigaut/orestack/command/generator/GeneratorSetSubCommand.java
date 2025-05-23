package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.generator.parameter.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorSetSubCommand extends SubCommand {

    public GeneratorSetSubCommand(@NotNull OrestackPlugin plugin) {
        super("set", plugin);
        withPermission(plugin.getPermission("generator.set"));
        withDescription(plugin.getLang("generator-set-command"));
        addParameter(new GeneratorNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "generator-not-found", placeholders);
                return;
            }
            final Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, "too-far-away", placeholders, generator);
                return;
            }
            final Location location = targetBlock.getLocation();
            try {
                Generator.create(generator, location);
            } catch (GeneratorOverlapException e) {
                plugin.sendMessage(player, "generator-overlap");
                return;
            }
            plugin.sendMessage(player, "created-generator", placeholders, generator);
        });
    }

}
