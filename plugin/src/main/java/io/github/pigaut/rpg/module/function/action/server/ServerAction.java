package io.github.pigaut.rpg.module.function.action.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface ServerAction extends Action {

    @NotNull
    FunctionResponse dispatch();

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return dispatch();
    }

    interface Executor extends ServerAction {

        void execute();

        @Override
        default @NotNull FunctionResponse dispatch() {
            execute();
            return FunctionResponse.NONE;
        }

    }

}
