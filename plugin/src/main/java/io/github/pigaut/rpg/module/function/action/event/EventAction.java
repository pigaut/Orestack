package io.github.pigaut.rpg.module.function.action.event;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public interface EventAction extends Action {

    void execute(@NotNull Event event);

    @Override
    default void execute(@NotNull Context context) {
        Event event = context.event();
        if (event != null) {
            execute(event);
        }
    }

}
