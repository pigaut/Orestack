package io.github.pigaut.rpg.module.function.condition.player.tool;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public interface ToolCondition extends Condition {

    @Nullable Boolean evaluate(@NotNull ItemStack tool);

    @Override
    default @Nullable Boolean isMet(@NotNull Context context) {
        ItemStack tool = context.tool();
        return tool != null ? evaluate(tool) : null;
    }

}
