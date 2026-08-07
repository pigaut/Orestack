package io.github.pigaut.rpg.module.function.action.mob.flag;

import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public class RemoveMobFlag implements MobAction {

    private final String flag;

    public RemoveMobFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        mob.removeFlag(flag);
    }
}
