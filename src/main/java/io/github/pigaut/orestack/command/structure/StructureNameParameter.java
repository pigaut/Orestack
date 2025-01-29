package io.github.pigaut.orestack.command.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.command.parameter.*;
import org.jetbrains.annotations.*;

public class StructureNameParameter extends CommandParameter {

    public StructureNameParameter(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("structure-name-parameter"),
                (sender, args) -> plugin.getStructures().getStructureNames());
    }

}
