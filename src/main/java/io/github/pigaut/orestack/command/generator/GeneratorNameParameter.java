package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.command.parameter.*;
import org.jetbrains.annotations.*;

public class GeneratorNameParameter extends CommandParameter {

    public GeneratorNameParameter(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("generator-name-parameter"),
                (sender, args) -> plugin.getGenerators().getGeneratorNames());
    }

}
