package io.github.pigaut.rpg.module.function.action.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface ServerAction extends Action {

    void execute();

    @Override
    default void execute(@NotNull Context context) {
        execute();
    }

}
