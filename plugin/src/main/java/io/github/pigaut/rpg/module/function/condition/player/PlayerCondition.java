package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerCondition extends Condition {

    @Nullable Boolean evaluate(@NotNull Player player);

    @Override
    default @Nullable Boolean isMet(@NotNull Context context) {
        Player player = context.player();
        return player != null ? evaluate(player) : null;
    }

}
