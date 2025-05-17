package io.github.pigaut.orestack.command.generator.parameter;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.command.parameter.*;
import org.jetbrains.annotations.*;

public class GeneratorGroupParameter extends CommandParameter {

    public GeneratorGroupParameter(@NotNull OrestackPlugin plugin) {
        super("generator-group", (sender, args) -> plugin.getGeneratorTemplates().getAllGroups());
    }

}
