package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public class MobHasFlag implements MobCondition.Predicate {

    private final String flag;

    public MobHasFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public boolean test(@NotNull Mob mob) {
        return mob.hasFlag(flag);
    }

}
