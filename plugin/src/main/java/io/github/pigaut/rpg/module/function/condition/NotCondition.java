package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class NotCondition implements Condition {

    private final Condition condition;

    public NotCondition(@NotNull Condition condition) {
        this.condition = condition;
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Context context) {
        FunctionResponse response = condition.evaluate(context);
        return switch (response.getType()) {
            case MET -> FunctionResponse.UNMET;
            case UNMET -> FunctionResponse.MET;
            default -> response;
        };
    }

}
