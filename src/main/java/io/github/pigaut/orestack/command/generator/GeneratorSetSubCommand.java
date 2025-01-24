package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorSetSubCommand extends LangSubCommand {

    public GeneratorSetSubCommand(@NotNull OrestackPlugin plugin) {
        super("set-generator", plugin);
        addParameter(new GeneratorNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final Generator generator = plugin.getGenerator(args[0]);
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
            BlockGenerator.create(generator, location);
            plugin.sendMessage(player, "created-generator", placeholders, generator);
        });
    }

}
