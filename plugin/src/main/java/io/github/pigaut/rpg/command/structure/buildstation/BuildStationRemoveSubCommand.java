package io.github.pigaut.rpg.command.structure.buildstation;

import io.github.pigaut.rpg.core.buildstation.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class BuildStationRemoveSubCommand extends SubCommand {

    public BuildStationRemoveSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "remove");
        withPermission(plugin.getPermission("buildstation.remove"));
        withDescription(plugin.getTranslation("buildstation-remove-command"));
        withPlayerExecution((player, context, args) -> {
            Block targetBlock = player.getTargetBlockExact(10);
            if (targetBlock == null) {
                plugin.sendMessage(player, context, "too-far-away");
                return;
            }

            Location location = targetBlock.getLocation();
            BuildStation builder = plugin.getBuildStation(location);
            if (builder == null) {
                plugin.sendMessage(player, context, "buildstation-not-found");
                return;
            }

            builder.remove();
            plugin.sendMessage(player, context, "removed-buildstation");
        });
    }

}
