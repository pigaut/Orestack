package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class PeriodicAction implements Action {

    private final EnhancedPlugin plugin;
    private final Action action;
    private final int interval;
    private final int repetitions;

    public PeriodicAction(EnhancedPlugin plugin, Action action, int interval, int repetitions) {
        this.plugin = plugin;
        this.action = action;
        this.interval = interval;
        this.repetitions = repetitions;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        action.dispatch(context);
        for (int i = 1; i < repetitions; i++) {
            long delay = (long) interval * i;
            plugin.getScheduler().runTaskLater(delay, () -> action.dispatch(context));
        }
        return FunctionResponse.NONE;
    }

}
