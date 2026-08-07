package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public interface MobAction extends Action {

    void execute(@NotNull Mob mob);

    @Override
    default void execute(@NotNull Context context) {
        Mob mob = context.mob();
        if (mob != null) {
            execute(mob);
        }
    }
}
