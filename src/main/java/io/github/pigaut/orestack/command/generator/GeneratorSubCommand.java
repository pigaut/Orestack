package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.server.*;
import org.jetbrains.annotations.*;

public class GeneratorSubCommand extends LangSubCommand {

    public GeneratorSubCommand(@NotNull OrestackPlugin plugin) {
        super("generator", plugin);
        addSubCommand(new GeneratorGetSubCommand(plugin));
        addSubCommand(new GeneratorGetAllSubCommand(plugin));
        addSubCommand(new GeneratorGetWandSubCommand(plugin));
        addSubCommand(new GeneratorSetSubCommand(plugin));
        addSubCommand(new GeneratorRemoveSubCommand(plugin));
        addSubCommand(new GeneratorSetAllSubCommand(plugin));
        addSubCommand(new GeneratorRemoveAllSubCommand(plugin));
        addSubCommand(new GeneratorHarvestSubCommand(plugin));
    }

}
