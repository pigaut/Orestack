package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public class MobNameEquals implements MobCondition.Predicate {

    private final String name;

    public MobNameEquals(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean test(@NotNull Mob mob) {
        return mob.getName().equals(name);
    }

}
