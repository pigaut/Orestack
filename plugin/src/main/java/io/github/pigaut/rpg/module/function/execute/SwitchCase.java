package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class SwitchCase implements Condition {

    private final Condition condition;
    private final Function function;

    public SwitchCase(@NotNull Condition condition, @NotNull Function function) {
        this.condition = condition;
        this.function = function;
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Context context) {
        return condition.evaluate(context);
    }

    public @NotNull Function getFunction() {
        return function;
    }
}
