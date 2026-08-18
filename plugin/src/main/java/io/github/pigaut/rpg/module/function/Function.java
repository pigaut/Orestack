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

public interface Function extends Identifiable {

    Function EMPTY = new SimpleFunction(Action.EMPTY);

    @NotNull
    FunctionResponse dispatch(@NotNull Context context);

    default void run(@NotNull Context context) {
        dispatch(context);
    }

}
