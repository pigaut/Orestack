package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.server.*;
import org.jetbrains.annotations.*;

public class GeneratorSubCommand extends SubCommand {

    public GeneratorSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("GENERATOR_COMMAND", "generator"), plugin);
        withPermission("orestack.generator");
        addSubCommand(new GeneratorGetSubCommand(plugin));
        addSubCommand(new GeneratorGetAllSubCommand(plugin));
        addSubCommand(new GeneratorSetSubCommand(plugin));
        addSubCommand(new GeneratorSetAllSubCommand(plugin));
        addSubCommand(new GeneratorRemoveAllSubCommand(plugin));
        addSubCommand(new GeneratorHarvestSubCommand(plugin));
    }

}
