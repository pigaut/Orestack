package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.player.data.*;
import org.jetbrains.annotations.*;

public interface PlayerDataCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull PlayerData playerData);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        PlayerData playerData = context.playerData();
        if (playerData == null) {
            return new FunctionError("Function was not triggered by a player with loaded data");
        }
        return evaluate(playerData);
    }

    @FunctionalInterface
    interface Predicate extends PlayerDataCondition {

        boolean test(@NotNull PlayerData playerData);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull PlayerData playerData) {
            return test(playerData) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}
