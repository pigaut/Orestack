package io.github.pigaut.rpg.module.function.yield;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.Function;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AmountYieldFunction implements YieldFunction<Amount> {

    private final Function function;

    public AmountYieldFunction(Function function) {
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
    public Amount yield(@NotNull Context context) {
        FunctionResponse response = function.dispatch(context);
        if (!(response instanceof YieldValueResponse yieldResponse)) {
            return null;
        }

        Object value = yieldResponse.getValue();
        if (value instanceof Amount amount) {
            return amount;
        }

        if (value instanceof Collection<?> elements) {
            double totalAmount = 0;

            for (Object element : elements) {
                if (element instanceof Amount amount) {
                    totalAmount += amount.doubleValue();
                }
            }
            return Amount.fixed(totalAmount);
        }

        return null;
    }

}