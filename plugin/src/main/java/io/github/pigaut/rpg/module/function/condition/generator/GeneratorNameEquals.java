package io.github.pigaut.rpg.module.function.condition.generator;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.*;
import org.jetbrains.annotations.*;

public class GeneratorNameEquals implements GeneratorCondition.Predicate {

    private final String name;

    public GeneratorNameEquals(String name) {
        this.name = name;
    }

    @Override
    public boolean test(@NotNull Generator generator) {
        return generator.getName().equals(name);
    }

}
