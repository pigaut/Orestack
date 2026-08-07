package io.github.pigaut.rpg.module.function.condition.server;

import io.github.pigaut.yaml.chance.*;

public class TestChance implements ServerCondition {

    private final Chance chance;

    public TestChance(Chance chance) {
        this.chance = chance;
    }

    @Override
    public Boolean evaluate() {
        return chance.test();
    }

}
