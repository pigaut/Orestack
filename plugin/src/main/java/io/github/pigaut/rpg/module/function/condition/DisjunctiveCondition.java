package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DisjunctiveCondition implements Condition {

    private final List<Condition> conditions;

    public DisjunctiveCondition(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        for (Condition condition : conditions) {
            Boolean met = condition.evaluate(context);
            if (met == null) {
                return null;
            }
            if (met) {
                return true;
            }
        }
        return false;
    }

}
