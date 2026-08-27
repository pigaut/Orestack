package io.github.pigaut.rpg.module.function;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.jetbrains.annotations.*;

public interface Function {

    Function EMPTY = context -> FunctionResponse.NONE;

    default boolean isGlobal() {
        return false;
    }

    @NotNull
    FunctionResponse dispatch(@NotNull Context context);

    default void run(@NotNull Context context) {
        dispatch(context);
    }

}
