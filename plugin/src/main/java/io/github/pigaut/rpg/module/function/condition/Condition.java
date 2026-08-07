package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface Condition {

    Condition EMPTY = context -> null;
    Condition MET = context -> true;
    Condition UNMET = context -> false;

    default boolean isMet(@NotNull Context context) {
        Boolean result = evaluate(context);
        return result != null ? result : false;
    }

    @Nullable Boolean evaluate(@NotNull Context context);

}
