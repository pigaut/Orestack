package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public interface Action {

    Action EMPTY = new Action() {};

    default void execute(@NotNull Context context) {}

    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        execute(context);
        return FunctionResponse.NONE;
    }

}
