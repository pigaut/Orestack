package io.github.pigaut.rpg.module.function.yield;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.Function;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BooleanYieldFunction implements YieldFunction<Boolean> {

    private final Function function;

    public BooleanYieldFunction(Function function) {
        this.function = function;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return function.dispatch(context);
    }

    @Override
    public @NotNull String getName() {
        return function.getName();
    }

    @Override
    public @Nullable String getGroup() {
        return function.getGroup();
    }

    @Override
    public Boolean yield(@NotNull Context context) {
        FunctionResponse response = function.dispatch(context);
        if (!(response instanceof YieldValueResponse yieldResponse)) {
            return null;
        }

        Object value = yieldResponse.getValue();
        if (value instanceof Boolean bool) {
            return bool;
        }

        if (value instanceof Collection<?> elements) {
            Boolean totalBool = null;

            for (Object element : elements) {
                if (element instanceof Boolean bool) {
                    totalBool = totalBool != null ? (totalBool && bool) : bool;
                }
            }

            return totalBool;
        }

        return null;
    }

}