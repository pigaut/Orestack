package io.github.pigaut.rpg.module.function.condition.block;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface BlockCondition extends Condition {

    @Nullable Boolean evaluate(@NotNull Block block);

    @Override
    default @Nullable Boolean isMet(@NotNull Context context) {
        Block block = context.block();
        return block != null ? evaluate(block) : null;
    }

}
