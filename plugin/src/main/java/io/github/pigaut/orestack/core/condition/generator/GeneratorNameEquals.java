package io.github.pigaut.orestack.core.condition.generator;

import io.github.pigaut.orestack.generator.*;
import org.jetbrains.annotations.*;

public class GeneratorNameEquals implements GeneratorCondition {

    private final String name;

    public GeneratorNameEquals(String name) {
        this.name = name;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Generator generator) {
        return generator.getName().equals(name);
    }

}
