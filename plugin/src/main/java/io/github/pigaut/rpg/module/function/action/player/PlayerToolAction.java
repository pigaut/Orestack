package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerToolAction extends Action {

    void execute(@NotNull Player player, @NotNull ItemStack tool);

    @Override
    default void execute(@NotNull Context context) {
        Player player = context.player();
        ItemStack tool = context.tool();
        if (player != null && tool != null) {
            execute(player, tool);
        }
    }

}
