package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public class MobHasFlag implements MobCondition {

    private final String flag;

    public MobHasFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public Boolean evaluate(@NotNull Mob mob) {
        return mob.hasFlag(flag);
    }

}
