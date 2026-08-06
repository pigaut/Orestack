package io.github.pigaut.rpg.command.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.node.*;
import org.jetbrains.annotations.*;

public class GateSubCommand extends SubCommand {

    public GateSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "gate");
        withPermission(plugin.getPermission("gate"));
        withDescription(plugin.getTranslation("gate-command"));
        addSubCommand(new GateGetSubCommand(plugin));
        addSubCommand(new GateGetGroupSubCommand(plugin));
        addSubCommand(new GateGetAllSubCommand(plugin));
        addSubCommand(new GateSetSubCommand(plugin));
        addSubCommand(new GateRemoveSubCommand(plugin));
        addSubCommand(new GateOpenSubCommand(plugin));
        addSubCommand(new GateCloseSubCommand(plugin));
    }

}
