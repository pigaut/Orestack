package io.github.pigaut.rpg.module.function.condition.event;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface EventCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull Event event);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        Event event = context.event();
        if (event == null) {
            return new FunctionError("Function was not triggered by an event");
        }
        return evaluate(event);
    }

    @FunctionalInterface
    interface Predicate extends EventCondition {

        boolean test(@NotNull Event event);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull Event event) {
            return test(event) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}
