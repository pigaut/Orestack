package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.mob.Mob;
import org.jetbrains.annotations.*;

public interface MobCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull Mob mob);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        Mob mob = context.mob();
        if (mob == null) {
            return new FunctionError("Event that triggered the function does not have a mob");
        }
        return evaluate(mob);
    }

    @FunctionalInterface
    interface Predicate extends MobCondition {

        boolean test(@NotNull Mob mob);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull Mob mob) {
            return test(mob) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}
