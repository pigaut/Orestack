package io.github.pigaut.rpg.module.function.condition.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface ServerCondition extends Condition {

    @Nullable Boolean evaluate();

    @Override
    default @Nullable Boolean isMet(@NotNull Context context) {
        return evaluate();
    }

}
