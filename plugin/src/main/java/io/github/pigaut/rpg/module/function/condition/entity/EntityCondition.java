package io.github.pigaut.rpg.module.function.condition.entity;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface EntityCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull Entity entity);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        Entity entity = context.enemy();
        if (entity == null) {
            return FunctionResponse.ERROR;
        }
        return evaluate(entity);
    }

    @FunctionalInterface
    interface Predicate extends EntityCondition {

        boolean test(@NotNull Entity entity);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull Entity entity) {
            return test(entity) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}