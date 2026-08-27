package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SwitchFunction implements Function {

    private final SwitchCase[] cases;
    private final Function defaultCase;

    public SwitchFunction(@NotNull Collection<SwitchCase> cases, @Nullable Function defaultCase) {
        this.cases = cases.toArray(new SwitchCase[0]);
        this.defaultCase = defaultCase;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        for (SwitchCase switchCase : cases) {
            Boolean met = switchCase.isMet(context);
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
