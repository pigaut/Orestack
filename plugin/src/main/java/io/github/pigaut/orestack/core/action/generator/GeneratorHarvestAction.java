package io.github.pigaut.orestack.core.action.generator;

import io.github.pigaut.orestack.generator.*;
import org.jetbrains.annotations.*;

public class GeneratorHarvestAction implements GeneratorAction {

    @Override
    public void execute(@NotNull Generator generator) {
        generator.harvest();
    }

}
