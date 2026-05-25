package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

public class GateHarvestSubCommand extends SubCommand {

    public GateHarvestSubCommand(@NotNull OrestackPlugin plugin) {
        super("harvest-all", plugin);
        withPermission(plugin.getPermission("gate.harvest-all"));
        withDescription(plugin.getTranslation("gate-harvest-all-command"));
        withParameter(GateParameters.GATE_NAME);
        withPlayerExecution((player, context, args) -> {
            final GateTemplate gate = plugin.getGateTemplate(args[0]);
            if (gate == null) {
                plugin.sendMessage(player, context, "gate-not-found");
                return;
            }
            for (Gate blockGate : plugin.getGates().getAllGates()) {
                if (blockGate.getTemplate() == gate) {
                    for (Block block : blockGate.getBlocks()) {
                        BlockBreakEvent event = new BlockBreakEvent(block, player);
                        Server.callEvent(event);
                        break;
                    }
                }
            }
            plugin.sendMessage(player, context, "harvested-all-gates");
        });
    }

}
