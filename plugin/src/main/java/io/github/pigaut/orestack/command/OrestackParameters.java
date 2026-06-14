package io.github.pigaut.orestack.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.core.command.*;

public class OrestackParameters {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    public static final CommandParameter GENERATOR_NAME = CommandParameter.create("generator-name",
            (commandSender, strings) -> plugin.getGeneratorTemplates().getAllNames());

    public static final CommandParameter GENERATOR_GROUP = CommandParameter.create("generator-group",
            (commandSender, strings) -> plugin.getGeneratorTemplates().getAllGroups());

    public static final CommandParameter GATE_NAME = CommandParameter.create("gate-name",
            (commandSender, strings) -> plugin.getGateTemplates().getAllNames());

    public static final CommandParameter GATE_GROUP = CommandParameter.create("gate-group",
            (commandSender, strings) -> plugin.getGateTemplates().getAllGroups());

    public static final CommandParameter COLLECTION_NAME = CommandParameter.create("collection-name",
            (sender, args) -> plugin.getCollectionTemplates().getAllNames());

}
