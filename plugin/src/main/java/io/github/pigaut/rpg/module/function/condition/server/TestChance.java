package io.github.pigaut.rpg.module.function.condition.server;

import io.github.pigaut.yaml.chance.*;
import org.jetbrains.annotations.*;

public class TestChance implements ServerCondition.Predicate {

    private final Chance chance;

    public TestChance(@NotNull Chance chance) {
        this.chance = chance;
    }

    @Override
    public boolean test() {
        return chance.test();
    }

}
