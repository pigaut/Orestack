package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.voxel.core.command.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateCloseSubCommand extends SubCommand {

    public GateCloseSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "close");
        withPermission(plugin.getPermission("gate.close"));
        withDescription(plugin.getTranslation("gate-close-command"));
        withParameter(CommandParameters.WORLD_NAME);
        withParameter(CommandParameters.X_COORDINATE);
        withParameter(CommandParameters.Y_COORDINATE);
        withParameter(CommandParameters.Z_COORDINATE);
        withCommandExecution((sender, context, args) -> {
            World world = Bukkit.getWorld(args[0]);
            if (world == null) {
                plugin.sendMessage(sender, context, "world-not-found");
                return;
            }

            Integer x = ParseUtil.parseIntegerOrNull(args[1]);
            Integer y = ParseUtil.parseIntegerOrNull(args[2]);
            Integer z = ParseUtil.parseIntegerOrNull(args[3]);

            if (x == null || y == null || z == null) {
                plugin.sendMessage(sender, context, "expected-coordinates");
                return;
            }

            Location location = new Location(world, x, y, z);

            Gate gate = plugin.getGate(location);
            if (gate == null) {
                plugin.sendMessage(sender, context, "block-not-gate");
                return;
            }

            gate.close();
            plugin.sendMessage(sender, context, "closed-gate");
        });

    }

}
