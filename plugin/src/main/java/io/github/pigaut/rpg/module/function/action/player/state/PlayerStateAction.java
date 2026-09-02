package io.github.pigaut.rpg.module.function.action.player.state;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerStateAction extends Action {

    @NotNull
    FunctionResponse dispatch(@NotNull Player player, @NotNull PlayerState playerState);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Player player = context.player();
        PlayerState playerState = context.playerState();
        if (player == null || playerState == null) {
            return new FunctionError("Function trigger does not support player state actions");
        }
        return dispatch(player, playerState);
    }

    @FunctionalInterface
    interface Executor extends PlayerStateAction {

        void execute(@NotNull Player player, @NotNull PlayerState playerState);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull Player player, @NotNull PlayerState playerState) {
            execute(player, playerState);
            return FunctionResponse.NONE;
        }

    }

}
