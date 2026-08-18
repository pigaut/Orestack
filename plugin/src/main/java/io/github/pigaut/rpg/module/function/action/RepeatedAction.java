package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class RepeatedAction implements Action {

    private final Action action;
    private final int repetitions;

    public RepeatedAction(Action action, int repetitions) {
        this.action = action;
        this.repetitions = repetitions;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        for (int i = 0; i < repetitions; i++) {
            FunctionResponse response = action.dispatch(context);
            if (response != FunctionResponse.NONE) {
                return response;
            }
        }
        return FunctionResponse.NONE;
    }

}
