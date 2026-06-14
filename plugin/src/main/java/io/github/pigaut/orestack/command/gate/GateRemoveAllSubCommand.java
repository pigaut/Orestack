package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.orestack.player.state.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.data.structure.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateRemoveAllSubCommand extends SubCommand {

    public GateRemoveAllSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "remove-all");
        withPermission(plugin.getPermission("gate.remove-all"));
        withDescription(plugin.getTranslation("gate-remove-all-command"));
        withParameter(OrestackParameters.GATE_NAME);
        withPlayerExecution((player, context, args) -> {
            RpgPlayerState playerState = plugin.getPlayerState(player);
            GateTemplate gateTemplate = plugin.getGateTemplate(args[0]);
            if (gateTemplate == null) {
                plugin.sendMessage(player, context, "gate-not-found");
                return;
            }
            Location firstSelection = playerState.getFirstSelection();
            Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, context, "incomplete-region");
                return;
            }
            for (Location point : CuboidRegion.getAllLocations(player.getWorld(), firstSelection, secondSelection)) {
                Gate gate = plugin.getGate(point);
                if (gate == null) {
                    continue;
                }
                if (gate.getTemplate() == gateTemplate) {
                    gate.remove();
                }
            }
            plugin.sendMessage(player, context, "removed-all-gates");
        });
    }

}
