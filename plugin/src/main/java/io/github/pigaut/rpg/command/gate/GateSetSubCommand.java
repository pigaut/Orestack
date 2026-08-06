package io.github.pigaut.rpg.command.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.gate.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.exception.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.gate.GatePlaceEvent;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.exception.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.server.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GateSetSubCommand extends SubCommand {

    public GateSetSubCommand(@NotNull RpgMakerPlugin plugin) {
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

            io.github.pigaut.rpg.api.event.gate.GatePlaceEvent gatePlaceEvent = new GatePlaceEvent(player, location, gate.getName(), gate.getOccupiedBlocks(location, Rotation.NONE));
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
