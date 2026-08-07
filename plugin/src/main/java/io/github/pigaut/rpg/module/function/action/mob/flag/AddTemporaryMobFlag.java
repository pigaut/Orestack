package io.github.pigaut.rpg.module.function.action.mob.flag;

import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public class AddTemporaryMobFlag implements MobAction {

    private final String flag;
    private final int duration;

    public AddTemporaryMobFlag(String flag, int duration) {
        this.flag = flag;
        this.duration = duration;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        mob.addTemporaryFlag(flag, duration);
    }

}
