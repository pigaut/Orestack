package io.github.pigaut.rpg.module.function.action.generator;

import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.*;
import org.jetbrains.annotations.*;

public class GeneratorGrowAction implements GeneratorAction.Executor {

    @Override
    public void execute(@NotNull Generator generator) {
        generator.grow();
    }

}
