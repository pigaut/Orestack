package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.mob.Mob;
import org.jetbrains.annotations.*;

public interface MobCondition extends Condition {

    Boolean evaluate(@NotNull Mob mob);

    @Override
    default @Nullable Boolean evaluate(@NotNull Context context) {
        Mob mob = context.mob();
        if (mob == null) {
            return null;
        }
        return evaluate(mob);
    }

}
