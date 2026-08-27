package io.github.pigaut.rpg.module.function.yield;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.Function;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FloatYieldFunction implements YieldFunction<Float> {

    private final Function function;

    public FloatYieldFunction(@NotNull Function function) {
        this.function = function;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return function.dispatch(context);
    }

    @Override
    public Float yield(@NotNull Context context) {
        FunctionResponse response = function.dispatch(context);
        if (!(response instanceof YieldValueResponse yieldResponse)) {
            return null;
        }

        Object value = yieldResponse.getValue();
        if (value instanceof Float floatValue) {
            return floatValue;
        }

        if (value instanceof Collection<?> elements) {
            float totalAmount = 0;

            for (Object element : elements) {
                if (element instanceof Float floatValue) {
                    totalAmount += floatValue;
                }
            }
            return totalAmount;
        }

        return null;
    }

}