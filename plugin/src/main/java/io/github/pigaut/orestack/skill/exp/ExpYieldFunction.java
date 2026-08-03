package io.github.pigaut.orestack.skill.exp;

import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.module.function.*;
import io.github.pigaut.voxel.module.function.Function;
import io.github.pigaut.voxel.module.function.response.*;
import io.github.pigaut.voxel.module.function.yield.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class ExpYieldFunction implements YieldFunction<ExpAmount> {

    private final Function function;

    public ExpYieldFunction(Function function) {
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
    public ExpAmount yield(@NotNull Context context) {
        FunctionResponse response = function.dispatch(context);
        if (!(response instanceof YieldValueResponse yieldResponse)) {
            return null;
        }

        Object value = yieldResponse.getValue();
        if (value instanceof ExpAmount expAmount) {
            return expAmount;
        }

        if (value instanceof Collection<?> elements) {
            int totalExp = 0;
            for (Object element : elements) {
                if (element instanceof ExpAmount amount) {
                    totalExp += amount.intValue();
                }
            }
            return new ExpAmount(Amount.fixed(totalExp), null);
        }

        return null;
    }

}
