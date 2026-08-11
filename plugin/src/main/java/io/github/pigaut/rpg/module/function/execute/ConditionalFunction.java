package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class ConditionalFunction implements Function {

    private final String name;
    private final String group;
    private final Condition condition;
    private final Function success;
    private final Function failure;

    public ConditionalFunction(String name, String group, Condition condition, Function success, Function failure) {
        this.name = name;
        this.group = group;
        this.condition = condition;
        this.success = success;
        this.failure = failure;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Boolean met = condition.evaluate(context);
        if (met == null) {
            return FunctionResponse.STOP;
        }
        if (met) {
            return success.dispatch(context);
        } else {
            return failure.dispatch(context);
        }
    }

}
