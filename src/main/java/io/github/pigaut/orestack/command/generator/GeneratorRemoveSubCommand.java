package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorRemoveSubCommand extends SubCommand {

    public GeneratorRemoveSubCommand(@NotNull OrestackPlugin plugin) {
        super("remove", plugin);
        withPermission(plugin.getPermission("generator.remove"));
        withDescription(plugin.getLang("generator-remove-command"));
        withPlayerExecution((player, args, placeholders) -> {
            final Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, "too-far-away", placeholders);
                return;
            }
            final Location location = targetBlock.getLocation();
            final Generator generator = plugin.getGenerator(location);
            if (generator == null) {
                plugin.sendMessage(player, "target-not-generator", placeholders);
                return;
            }
            plugin.getGenerators().removeGenerator(generator);
            plugin.sendMessage(player, "removed-generator", placeholders, generator);
        });

    }

}
