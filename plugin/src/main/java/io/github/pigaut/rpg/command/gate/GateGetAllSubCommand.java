package io.github.pigaut.rpg.command.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.tools.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tools.*;
import io.github.pigaut.rpg.module.gate.template.*;
import org.jetbrains.annotations.*;

public class GateGetAllSubCommand extends SubCommand {

    public GateGetAllSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "get-all");
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
