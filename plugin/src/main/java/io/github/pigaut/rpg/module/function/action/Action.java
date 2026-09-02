package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public interface Action {

    Action EMPTY = context -> FunctionResponse.NONE;

    @NotNull
    FunctionResponse dispatch(@NotNull Context context);

    interface Executor extends Action {

        void execute(@NotNull Context context);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull Context context) {
            execute(context);
            return FunctionResponse.NONE;
        }
    }

}
