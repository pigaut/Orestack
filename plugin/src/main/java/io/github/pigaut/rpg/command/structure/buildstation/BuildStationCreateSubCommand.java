package io.github.pigaut.rpg.command.structure.buildstation;

import io.github.pigaut.rpg.core.buildstation.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BuildStationCreateSubCommand extends SubCommand {

    public BuildStationCreateSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "create");
        withPermission(plugin.getPermission("buildstation.create"));
        withDescription(plugin.getTranslation("buildstation-create-command"));

        withParameter(CommandParameter.create("size-x", "3", (sender, args) -> List.of("10", "15", "20", "25", "30")));
        withParameter(CommandParameter.create("size-y", "3", (sender, args) -> List.of("10", "15", "20", "25", "30")));
        withParameter(CommandParameter.create("size-z", "3", (sender, args) -> List.of("10", "15", "20", "25", "30")));

        withPlayerExecution((player, context, args) -> {
            Block targetBlock = player.getTargetBlockExact(10);
            if (targetBlock == null) {
                plugin.sendMessage(player, context, "too-far-away");
                return;
            }

            Integer sizeX = ParseUtil.parseIntegerOrNull(args[0]);
            Integer sizeY = ParseUtil.parseIntegerOrNull(args[1]);
            Integer sizeZ = ParseUtil.parseIntegerOrNull(args[2]);
            if (sizeX == null || sizeY == null || sizeZ == null) {
                plugin.sendMessage(player, context, "invalid-buildstation-size");
                return;
            }

            if (sizeX < 3 || sizeY < 3 || sizeZ < 3) {
                plugin.sendMessage(player, context, "minimum-buildstation-size");
                return;
            }

            Location location = targetBlock.getLocation().add(0, 1, 0);
            BuildStation builder = new BuildStation(plugin, location, sizeX, sizeY, sizeZ);
            builder.create();
            plugin.sendMessage(player, context, "created-buildstation");
        });
    }

}
