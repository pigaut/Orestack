package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface Condition {

    Condition ERROR = context -> FunctionResponse.ERROR;
    Condition MET = context -> FunctionResponse.MET;
    Condition UNMET = context -> FunctionResponse.UNMET;

    @NotNull
    FunctionResponse evaluate(@NotNull Context context);

    @FunctionalInterface
    interface Predicate extends Condition {

        boolean test(@NotNull Context context);

        @NotNull
        default FunctionResponse evaluate(@NotNull Context context) {
            boolean result = test(context);
            return result ? FunctionResponse.MET : FunctionResponse.UNMET;
        }
    }

}
