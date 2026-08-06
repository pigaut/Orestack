package io.github.pigaut.rpg.command.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateOpenSubCommand extends SubCommand {

    public GateOpenSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "open");
        withPermission(plugin.getPermission("gate.open"));
        withDescription(plugin.getTranslation("gate-open-command"));
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

            gate.open();
            plugin.sendMessage(sender, context, "opened-gate");
        });

    }

}
