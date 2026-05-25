package io.github.pigaut.orestack.command.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.core.command.*;

public class GateParameters {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    public static final CommandParameter GATE_NAME = CommandParameter.create("gate-name",
            (commandSender, strings) -> plugin.getGateTemplates().getAllNames());

    public static final CommandParameter GATE_GROUP = CommandParameter.create("gate-group",
            (commandSender, strings) -> plugin.getGateTemplates().getAllGroups());

}
