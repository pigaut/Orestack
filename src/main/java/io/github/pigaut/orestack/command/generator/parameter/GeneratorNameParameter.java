package io.github.pigaut.orestack.command.generator.parameter;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.command.parameter.*;
import org.jetbrains.annotations.*;

public class GeneratorNameParameter extends CommandParameter {

    public GeneratorNameParameter(@NotNull OrestackPlugin plugin) {
        super("generator-name", (sender, args) -> plugin.getGeneratorTemplates().getAllNames());
    }

}
