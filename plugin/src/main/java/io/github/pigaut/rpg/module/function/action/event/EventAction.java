package io.github.pigaut.rpg.module.function.action.event;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface EventAction extends Action {

    @NotNull
    FunctionResponse dispatch(@NotNull Event event);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Event event = context.event();
        if (event == null) {
            return new FunctionError("Function trigger does not support event actions");
        }
        return dispatch(event);
    }

    @FunctionalInterface
    interface Executor extends EventAction {

        void execute(@NotNull Event event);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull Event event) {
            execute(event);
            return FunctionResponse.NONE;
        }

    }

}
