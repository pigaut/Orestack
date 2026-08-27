package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.jetbrains.annotations.*;

public class GlobalFunction implements Function, Identifiable {

    private final String name;
    private final String group;
    private final Function function;

    public GlobalFunction(@NotNull String name, @Nullable String group, @NotNull Function function) {
        this.name = name;
        this.group = group;
        this.function = function;
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
    public boolean isGlobal() {
        return true;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return function.dispatch(context);
    }

}
