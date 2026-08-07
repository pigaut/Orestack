package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerAction extends Action {

    void execute(@NotNull Player player);

    @Override
    default void execute(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            execute(player);
        }
    }

}
