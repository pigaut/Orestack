package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface Action extends DispatchableAction {

    Action EMPTY = context -> {};

    void execute(@NotNull Context context);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        execute(context);
        return FunctionResponse.NONE;
    }

}
