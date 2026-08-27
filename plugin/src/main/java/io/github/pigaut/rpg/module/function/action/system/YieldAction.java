package io.github.pigaut.rpg.module.function.action.system;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class YieldAction implements Action {

    private final FunctionResponse returnValueResponse;

    public YieldAction(@Nullable Object value) {
        this.returnValueResponse = new YieldValueResponse(value);
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return returnValueResponse;
    }

}
