package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiAction implements Action {

    private final List<Action> actions;

    public MultiAction(@NotNull List<@NotNull Action> actions) {
        this.actions = actions;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        for (Action action : actions) {
            FunctionResponse response = action.dispatch(context);
            if (response != FunctionResponse.NONE) {
                return response;
            }
        }
        return FunctionResponse.NONE;
    }

}
