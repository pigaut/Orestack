package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerAction extends Action {

    @NotNull
    FunctionResponse dispatch(@NotNull Player player);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Player player = context.player();
        if (player == null) {
            return new FunctionError("Function was not triggered by a player");
        }
        return dispatch(player);
    }

    @FunctionalInterface
    interface Executor extends PlayerAction {

        void execute(@NotNull Player player);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull Player player) {
            execute(player);
            return FunctionResponse.NONE;
        }

    }

}
