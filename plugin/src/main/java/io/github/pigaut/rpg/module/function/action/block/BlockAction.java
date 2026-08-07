package io.github.pigaut.rpg.module.function.action.block;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface BlockAction extends Action {

    void execute(@NotNull Block block);

    @Override
    default void execute(@NotNull Context context) {
        Block block = context.block();
        if (block != null) {
            execute(block);
        }
    }

}
