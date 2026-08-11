package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public class MobNameEquals implements MobCondition {

    private final String name;

    public MobNameEquals(String name) {
        this.name = name;
    }

    @Override
    public Boolean evaluate(@NotNull Mob mob) {
        return mob.getName().equals(name);
    }

}
