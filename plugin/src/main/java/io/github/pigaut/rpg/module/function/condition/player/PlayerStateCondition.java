package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public interface PlayerStateCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull PlayerState playerState);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        PlayerState playerState = context.playerState();
        if (playerState == null) {
            return new FunctionError("Function was not triggered by a player with an active state");
        }
        return evaluate(playerState);
    }

    @FunctionalInterface
    interface Predicate extends PlayerStateCondition {

        boolean test(@NotNull PlayerState playerState);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull PlayerState playerState) {
            return test(playerState) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}
