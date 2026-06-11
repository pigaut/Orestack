package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.gate.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.gate.exception.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.util.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GateSetSubCommand extends SubCommand {

    public GateSetSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "set");
        withPermission(plugin.getPermission("gate.set"));
        withDescription(plugin.getTranslation("gate-set-command"));
        withParameter(OrestackParameters.GATE_NAME);
        withPlayerExecution((player, context, args) -> {
            GateTemplate gate = plugin.getGateTemplate(args[0]);
            if (gate == null) {
                plugin.sendMessage(player, context, "gate-not-found");
                return;
            }

            Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, context, "too-far-away");
                return;
            }

            Location location = targetBlock.getLocation();

            GatePlaceEvent gatePlaceEvent = new GatePlaceEvent(player, location, gate.getName(), gate.getOccupiedBlocks(location, Rotation.NONE));
            Server.callEvent(gatePlaceEvent);

            if (gatePlaceEvent.isCancelled()) {
                plugin.sendMessage(player, context, "gate-conflict");
                return;
            }

            try {
                Gate.create(gate, location);
                plugin.sendMessage(player, context, "created-gate");
            }
            catch (GateCreateException e) {
                PlayerUtil.sendChat(player, context, e.getMessage());
            }
        });
    }

}
