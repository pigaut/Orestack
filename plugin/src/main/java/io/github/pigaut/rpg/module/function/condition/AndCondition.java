package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

// Resolves as FunctionResponse.MET if all condition are true, unmet/error otherwise.
public class AndCondition implements Condition {

    private final Condition[] conditions;

    public AndCondition(@NotNull Collection<Condition> conditions) {
        this.conditions = conditions.toArray(new Condition[0]);
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Context context) {
        for (Condition condition : conditions) {
            FunctionResponse response = condition.evaluate(context);
            if (response.getType() == ResponseType.MET) {
                continue;
            }
            return response;
        }
        return FunctionResponse.MET;
    }

}
