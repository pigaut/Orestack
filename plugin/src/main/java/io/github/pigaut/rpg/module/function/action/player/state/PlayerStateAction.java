package io.github.pigaut.rpg.module.function.action.player.state;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerStateAction extends Action {

    void execute(@NotNull Player player, @NotNull PlayerState playerState);

    @Override
    default void execute(@NotNull Context context) {
        Player player = context.player();
        PlayerState playerState = context.playerState();
        if (player != null && playerState != null) {
            execute(player, playerState);
        }
    }

}
