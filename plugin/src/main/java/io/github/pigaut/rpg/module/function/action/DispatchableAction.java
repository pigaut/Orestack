package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface DispatchableAction {

    DispatchableAction EMPTY = context -> FunctionResponse.NONE;

    @NotNull
    FunctionResponse dispatch(@NotNull Context context);

}
