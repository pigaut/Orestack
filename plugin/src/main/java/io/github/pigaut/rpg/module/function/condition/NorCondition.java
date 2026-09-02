package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

// Resolves as FunctionResponse.MET if all conditions are unmet, unmet/error otherwise
public class NorCondition implements Condition {

    private final Condition[] conditions;

    public NorCondition(@NotNull Collection<Condition> conditions) {
        this.conditions = conditions.toArray(new Condition[0]);
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Context context) {
        for (Condition condition : conditions) {
            FunctionResponse response = condition.evaluate(context);
            ResponseType responseType = response.getType();
            if (responseType == ResponseType.UNMET) {
                continue;
            }
            if (responseType == ResponseType.MET) {
                return FunctionResponse.UNMET;
            }
            return response;
        }
        return FunctionResponse.MET;
    }

}
