package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.voxel.core.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GateRemoveSubCommand extends SubCommand {

    public GateRemoveSubCommand(@NotNull OrestackPlugin plugin) {
        super("remove", plugin);
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
