package io.github.pigaut.rpg.module.function.yield;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.Function;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DelayYieldFunction implements YieldFunction<Delay> {

    private final Function function;

    public DelayYieldFunction(@NotNull Function function) {
        this.function = function;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return function.dispatch(context);
    }

    @Override
    public Delay yield(@NotNull Context context) {
        FunctionResponse response = function.dispatch(context);
        if (!(response instanceof YieldValueResponse yieldResponse)) {
            return null;
        }

        Object value = yieldResponse.getValue();
        if (value instanceof Delay delay) {
            return delay;
        }

        if (value instanceof Collection<?> elements) {
            int totalDelay = 0;

            for (Object element : elements) {
                if (element instanceof Delay delay) {
                    totalDelay += delay.toTicks();
                }
            }

            return Delay.fromTicks(totalDelay);
        }

        return null;
    }

}