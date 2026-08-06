package io.github.pigaut.rpg.command.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.gate.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GateRemoveSubCommand extends SubCommand {

    public GateRemoveSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "remove");
        withPermission(plugin.getPermission("gate.remove"));
        withDescription(plugin.getTranslation("gate-remove-command"));
        withPlayerExecution((player, context, args) -> {
            Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, context, "too-far-away");
                return;
            }
            Location location = targetBlock.getLocation();
            Gate gate = plugin.getGate(location);
            if (gate == null) {
                plugin.sendMessage(player, context, "target-not-gate");
                return;
            }
            gate.remove();
            plugin.sendMessage(player, context, "removed-gate");
        });

    }

}
