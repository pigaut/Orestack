package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.gate.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.gate.exception.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.orestack.player.state.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.data.structure.*;
import io.github.pigaut.voxel.util.Server;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateSetAllSubCommand extends SubCommand {

    public GateSetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "set-all");
        withPermission(plugin.getPermission("gate.set-all"));
        withDescription(plugin.getTranslation("gate-set-all-command"));
        withParameter(OrestackParameters.GATE_NAME);
        withPlayerExecution((player, context, args) -> {
            RpgPlayerState playerState = plugin.getPlayerState(player);
            GateTemplate template = plugin.getGateTemplate(args[0]);
            if (template == null) {
                plugin.sendMessage(player, context, "gate-not-found");
                return;
            }
            Location firstSelection = playerState.getFirstSelection();
            Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, context, "incomplete-region");
                return;
            }
            StructureTemplate structure = template.getLastPhase().getStructureTemplate();
            for (Location location : CuboidRegion.getAllLocations(player.getWorld(), firstSelection, secondSelection)) {
                for (Rotation rotation : Rotation.values()) {
                    if (structure.isPlaced(location, rotation)) {
                        GatePlaceEvent gatePlaceEvent = new GatePlaceEvent(player, location, template.getName(), template.getOccupiedBlocks(location, Rotation.NONE));
                        Server.callEvent(gatePlaceEvent);

                        if (gatePlaceEvent.isCancelled()) {
                            continue;
                        }

                        try {
                            Gate.create(template, location);
                        }
                        catch (GateCreateException ignored) {
                            // Ignore if gate overlaps
                        }
                    }
                }
            }
            plugin.sendMessage(player, context, "created-all-gates");
        });
    }

}
