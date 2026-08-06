package io.github.pigaut.rpg.command.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.tools.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.tools.*;
import io.github.pigaut.rpg.module.gate.template.*;
import org.jetbrains.annotations.*;

public class GateGetSubCommand extends SubCommand {

    public GateGetSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "get");
        withPermission(plugin.getPermission("gate.get"));
        withDescription(plugin.getTranslation("gate-get-command"));
        withParameter(OrestackParameters.GATE_NAME);
        withPlayerExecution((player, context, args) -> {
            GateTemplate gate = plugin.getGateTemplate(args[0]);
            if (gate == null) {
                plugin.sendMessage(player, context, "gate-not-found");
                return;
            }
            PlayerUtil.giveItemsOrDrop(player, GateTool.createItem(gate));
            plugin.sendMessage(player, context, "received-gate");
        });
    }

}
