package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.core.tools.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.command.node.*;
import org.jetbrains.annotations.*;

public class GateGetAllSubCommand extends SubCommand {

    public GateGetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super("get-all", plugin);
        withPermission(plugin.getPermission("gate.get-all"));
        withDescription(plugin.getTranslation("gate-get-all-command"));
        withPlayerExecution((player, context, args) -> {
            for (GateTemplate gate : plugin.getGateTemplates().getAll()) {
                PlayerUtil.giveItemsOrDrop(player, GateTool.createItem(gate));
            }
            plugin.sendMessage(player, context, "received-all-gates");
        });
    }
}
