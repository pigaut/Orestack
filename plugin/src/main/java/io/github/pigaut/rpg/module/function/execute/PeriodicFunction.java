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

public class PeriodicFunction implements Function {

    private final EnhancedPlugin plugin;
    private final Function function;
    private final int interval;
    private final int repetitions;

    public PeriodicFunction(@NotNull EnhancedPlugin plugin, @NotNull Function function, int interval, int repetitions) {
        this.plugin = plugin;
        this.function = function;
        this.interval = interval;
        this.repetitions = repetitions;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        function.dispatch(context);
        for (int i = 1; i < repetitions; i++) {
            long delay = (long) interval * i;
            plugin.getScheduler().runTaskLater(delay, () -> function.dispatch(context));
        }
        return FunctionResponse.NONE;
    }

}
