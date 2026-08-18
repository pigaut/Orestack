package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface PlayerAction extends Action {

    default void execute(@NotNull Player player) {}

    @NotNull
    default FunctionResponse dispatch(@NotNull Player player) {
        execute(player);
        return FunctionResponse.NONE;
    }

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            return dispatch(player);
        }
        return FunctionResponse.NONE;
    }

}
