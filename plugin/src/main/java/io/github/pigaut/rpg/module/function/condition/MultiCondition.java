package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiCondition implements Condition {

    private final List<Condition> conditions;

    public MultiCondition(@NotNull List<@NotNull Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        for (Condition condition : conditions) {
            Boolean met = condition.evaluate(context);
            if (met == null) {
                return null;
            }
            if (!met) {
                return false;
            }
        }
        return true;
    }

}
