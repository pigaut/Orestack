package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class SwitchFunction implements Function {

    private final String name;
    private final String group;
    private final SwitchCase[] cases;
    private final Function defaultCase;

    public SwitchFunction(@NotNull String name, @Nullable String group,
                          @NotNull SwitchCase[] cases, @Nullable Function defaultCase) {
        this.name = name;
        this.group = group;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        for (SwitchCase switchCase : cases) {
            Boolean met = switchCase.evaluate(context);
            if (met == null) {
                return FunctionResponse.NONE;
            }

            if (met) {
                Function function = switchCase.getFunction();
                if (function != null) {
                    return function.dispatch(context);
                }
                return FunctionResponse.NONE;
            }
        }

        if (defaultCase != null) {
            return defaultCase.dispatch(context);
        }

        return FunctionResponse.NONE;
    }

}
