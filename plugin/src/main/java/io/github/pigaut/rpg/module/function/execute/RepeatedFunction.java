package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class RepeatedFunction implements Function {

    private final Function function;
    private final int repetitions;

    public RepeatedFunction(Function function, int repetitions) {
        this.function = function;
        this.repetitions = repetitions;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        for (int i = 0; i < repetitions; i++) {
            FunctionResponse response = function.dispatch(context);
            if (response != FunctionResponse.NONE) {
                return response;
            }
        }
        return FunctionResponse.NONE;
    }

}
