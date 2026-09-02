package io.github.pigaut.rpg.module.function.action.mob.flag;

import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

public class AddTemporaryMobFlag implements MobAction.Executor {

    private final String flag;
    private final Delay duration;

    public AddTemporaryMobFlag(@NotNull String flag, @NotNull Delay duration) {
        this.flag = flag;
        this.duration = duration;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        mob.addTemporaryFlag(flag, duration);
    }

}
