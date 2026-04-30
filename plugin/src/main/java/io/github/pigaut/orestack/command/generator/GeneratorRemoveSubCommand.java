package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.voxel.core.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorRemoveSubCommand extends SubCommand {

    public GeneratorRemoveSubCommand(@NotNull OrestackPlugin plugin) {
        super("remove", plugin);
        withPermission(plugin.getPermission("generator.remove"));
        withDescription(plugin.getTranslation("generator-remove-command"));
        withPlayerExecution((player, context, args) -> {
            Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, context, "too-far-away");
                return;
            }
            Location location = targetBlock.getLocation();
            Generator generator = plugin.getGenerator(player, location);
            if (generator == null) {
                plugin.sendMessage(player, context, "target-not-generator");
                return;
            }

            generator.remove();
            plugin.sendMessage(player, context, "removed-generator");
        });

    }

}
