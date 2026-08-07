package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiAction implements DispatchableAction {

    private final List<DispatchableAction> actions;

    public MultiAction(@NotNull List<@NotNull DispatchableAction> actions) {
        this.actions = actions;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        for (DispatchableAction action : actions) {
            FunctionResponse response = action.dispatch(context);
            if (response != FunctionResponse.NONE) {
                return response;
            }
        }
        return FunctionResponse.NONE;
    }

}
