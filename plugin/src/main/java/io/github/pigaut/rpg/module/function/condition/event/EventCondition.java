package io.github.pigaut.rpg.module.function.condition.event;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface EventCondition extends Condition {

    @Nullable Boolean evaluate(@NotNull Event event);

    @Override
    default @Nullable Boolean evaluate(@NotNull Context context) {
        Event event = context.event();
        return event != null ? evaluate(event) : null;
    }

}
