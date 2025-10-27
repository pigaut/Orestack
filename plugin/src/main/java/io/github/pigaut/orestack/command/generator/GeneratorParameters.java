package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.command.*;

public class GeneratorParameters {

    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public static final CommandParameter GENERATOR_NAME = CommandParameter.create("generator-name",
            (commandSender, strings) -> plugin.getGeneratorTemplates().getAllNames());

    public static final CommandParameter GENERATOR_GROUP = CommandParameter.create("generator-group",
            (commandSender, strings) -> plugin.getGeneratorTemplates().getAllGroups());

}
