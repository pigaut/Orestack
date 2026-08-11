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

    private final String name;
    private final String group;
    private final List<Function> functions;

    public MultiFunction(String name, String group, @NotNull List<@NotNull Function> functions) {
        this.name = name;
        this.group = group;
        this.functions = functions;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        for (int i = 0; i < functions.size(); i++) {
            FunctionResponse response = functions.get(i).dispatch(context);

            ResponseType type = response.getType();
            if (type == ResponseType.RETURN) {
                return response;
            }
            if (type == ResponseType.STOP) {
                return response;
            }
            if (type == ResponseType.YIELD) {
                return response;
            }
            if (response instanceof GotoResponse gotoResponse) {
                int gotoLine = gotoResponse.getLine();
                if (gotoLine < functions.size()) {
                    i = gotoResponse.getLine();
                }
            }
        }

        return FunctionResponse.NONE;
    }

}
