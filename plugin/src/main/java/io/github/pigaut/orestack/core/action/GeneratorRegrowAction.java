package io.github.pigaut.orestack.core.action;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.voxel.data.function.action.*;
import io.github.pigaut.voxel.data.function.action.block.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class GeneratorRegrowAction implements GeneratorAction {

    @Override
    public void execute(@NotNull Generator generator) {
        generator.regrow();
    }

}
