package io.github.pigaut.rpg.module.function.action.mob.cooldown;

import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.Mob;
import org.jetbrains.annotations.*;

public class RemoveMobCooldown implements MobAction.Executor {

    private final String name;

    public RemoveMobCooldown(@NotNull String name) {
        this.name = name;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        mob.removeCooldown(name);
    }
}
