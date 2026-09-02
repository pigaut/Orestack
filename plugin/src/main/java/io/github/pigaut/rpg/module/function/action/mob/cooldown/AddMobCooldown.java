package io.github.pigaut.rpg.module.function.action.mob.cooldown;

import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

public class AddMobCooldown implements MobAction.Executor {

    private final String name;
    private final Delay duration;

    public AddMobCooldown(@NotNull String name, @NotNull Delay duration) {
        this.name = name;
        this.duration = duration;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        mob.addCooldown(name, duration);
    }

}
