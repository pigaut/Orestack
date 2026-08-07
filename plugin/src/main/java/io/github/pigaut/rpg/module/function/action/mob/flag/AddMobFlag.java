package io.github.pigaut.rpg.module.function.action.mob.flag;

import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public class AddMobFlag implements MobAction {

    private final String flag;

    public AddMobFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        mob.addFlag(flag);
    }

}
