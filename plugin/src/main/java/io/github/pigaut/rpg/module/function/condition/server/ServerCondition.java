package io.github.pigaut.rpg.module.function.condition.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface ServerCondition extends Condition {

    @NotNull
    FunctionResponse evaluate();

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        return evaluate();
    }

    @FunctionalInterface
    interface Predicate extends ServerCondition {

        boolean test();

        @Override
        default @NotNull FunctionResponse evaluate() {
            return test() ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}
