package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiFunction implements Function {

    private final Function[] functions;

    public MultiFunction(@NotNull Collection<Function> functions) {
        this.functions = functions.toArray(new Function[0]);
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        for (Function function : functions) {
            FunctionResponse response = function.dispatch(context);
            ResponseType type = response.getType();
            if (type == ResponseType.RETURN) {
                if (function.isGlobal()) {
                    continue;
                }
                return response;
            }

            if (type == ResponseType.CONTINUE) {
                continue;
            }

            if (type.isStopEarly()) {
                return response;
            }
        }

        return FunctionResponse.NONE;
    }

}
