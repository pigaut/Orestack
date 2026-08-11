package io.github.pigaut.rpg.command.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.module.gate.tool.*;
import org.jetbrains.annotations.*;

public class GateGetAllSubCommand extends SubCommand {

    public GateGetAllSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "get-all");
        withPermission(plugin.getPermission("gate.get-all"));
        withDescription(plugin.getTranslation("gate-get-all-command"));
        withPlayerExecution((player, context, args) -> {
            Tool tool = plugin.getTool("gate");
            if (!(tool instanceof GateTool gateTool)) {
                plugin.sendMessage(player, context, "tool-not-found");
                return;
            }

            for (GateTemplate gate : plugin.getGateTemplates().getAll()) {
                PlayerUtil.giveItemsOrDrop(player, gateTool.createItem(gate));
            }
            plugin.sendMessage(player, context, "received-all-gates");
        });
    }
}
