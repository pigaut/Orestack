package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.core.command.node.*;
import org.jetbrains.annotations.*;

public class GateSubCommand extends SubCommand {

    public GateSubCommand(@NotNull OrestackPlugin plugin) {
        super("gate", plugin);
        withPermission(plugin.getPermission("gate"));
        withDescription(plugin.getTranslation("gate-command"));
        addSubCommand(new GateGetSubCommand(plugin));
        addSubCommand(new GateGetGroupSubCommand(plugin));
        addSubCommand(new GateGetAllSubCommand(plugin));
        addSubCommand(new GateSetSubCommand(plugin));
        addSubCommand(new GateRemoveSubCommand(plugin));
        addSubCommand(new GateSetAllSubCommand(plugin));
        addSubCommand(new GateRemoveAllSubCommand(plugin));
        addSubCommand(new GateHarvestSubCommand(plugin));
    }

}
