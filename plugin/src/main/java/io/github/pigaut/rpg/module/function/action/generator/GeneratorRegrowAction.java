package io.github.pigaut.rpg.module.function.action.generator;

import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.*;
import org.jetbrains.annotations.*;

public class GeneratorRegrowAction implements GeneratorAction.Executor {

    @Override
    public void execute(@NotNull Generator generator) {
        generator.regrow();
    }

}
