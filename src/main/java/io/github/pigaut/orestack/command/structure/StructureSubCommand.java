package io.github.pigaut.orestack.command.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.generator.*;
import io.github.pigaut.voxel.command.node.*;
import org.jetbrains.annotations.*;

public class StructureSubCommand extends LangSubCommand {

    public StructureSubCommand(@NotNull OrestackPlugin plugin) {
        super("structure", plugin);
        addSubCommand(new StructureSaveSubCommand(plugin));
        addSubCommand(new StructurePlaceSubCommand(plugin));
    }

}
