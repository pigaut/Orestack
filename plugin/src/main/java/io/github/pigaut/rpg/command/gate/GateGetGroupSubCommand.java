package io.github.pigaut.rpg.command.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.module.gate.tool.*;
import io.github.pigaut.rpg.module.generator.tool.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateGetGroupSubCommand extends SubCommand {

    public GateGetGroupSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "get-group");
        withPermission(plugin.getPermission("gate.get-group"));
        withDescription(plugin.getTranslation("gate-get-group-command"));
        withParameter(OrestackParameters.GATE_GROUP);
        withPlayerExecution((player, context, args) -> {
            Tool tool = plugin.getTool("gate");
            if (!(tool instanceof GateTool gateTool)) {
                plugin.sendMessage(player, context, "tool-not-found");
                return;
            }

            List<GateTemplate> groupGates = plugin.getGateTemplates().getAll(args[0]);
            if (groupGates.isEmpty()) {
                plugin.sendMessage(player, context, "gate-group-not-found");
                return;
            }

            for (GateTemplate gate : groupGates) {
                PlayerUtil.giveItemsOrDrop(player, gateTool.createItem(gate));
            }
            plugin.sendMessage(player, context, "received-gate-group");
        });
    }

}
