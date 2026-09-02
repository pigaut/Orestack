package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull Player player);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        Player player = context.player();
        if (player == null) {
            return new FunctionError("Function trigger does not support player conditions");
        }
        return evaluate(player);
    }

    @FunctionalInterface
    interface Predicate extends PlayerCondition {

        boolean test(@NotNull Player player);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull Player player) {
            return test(player) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}
