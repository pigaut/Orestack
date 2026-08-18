package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

public class SimpleFunction implements Function {

    private final String name;
    private final String group;
    private final Action action;

    public SimpleFunction(@NotNull Action action) {
        this(YamlConfig.generateRandomKey(), null, action);
    }

    public SimpleFunction(String name, String group, @NotNull Action action) {
        this.name = name;
        this.group = group;
        this.action = action;
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
        return action.dispatch(context);
    }

}
