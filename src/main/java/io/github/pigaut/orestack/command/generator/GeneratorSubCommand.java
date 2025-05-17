package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.voxel.command.node.*;
import org.jetbrains.annotations.*;

public class GeneratorSubCommand extends SubCommand {

    public GeneratorSubCommand(@NotNull OrestackPlugin plugin) {
        super("generator", plugin);
        withPermission(plugin.getPermission("generator"));
        withDescription(plugin.getLang("generator-command"));
        addSubCommand(new GeneratorGetSubCommand(plugin));
        addSubCommand(new GeneratorGetGroupSubCommand(plugin));
        addSubCommand(new GeneratorGetAllSubCommand(plugin));
        addSubCommand(new GeneratorSetSubCommand(plugin));
        addSubCommand(new GeneratorRemoveSubCommand(plugin));
        addSubCommand(new GeneratorSetAllSubCommand(plugin));
        addSubCommand(new GeneratorRemoveAllSubCommand(plugin));
        addSubCommand(new GeneratorHarvestSubCommand(plugin));
    }

}
