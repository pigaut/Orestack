package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class DelayedFunction implements Function {

    private final EnhancedPlugin plugin;
    private final Function function;
    private final int delay;

    public DelayedFunction(@NotNull EnhancedPlugin plugin, @NotNull Function function, int delay) {
        this.plugin = plugin;
        this.function = function;
        this.delay = delay;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        plugin.getScheduler().runTaskLater(delay, () -> function.dispatch(context));
        return FunctionResponse.NONE;
    }

}
