package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class DelayedAction implements Action {

    private final EnhancedPlugin plugin;
    private final Action action;
    private final int delay;

    public DelayedAction(EnhancedPlugin plugin, Action action, int delay) {
        this.plugin = plugin;
        this.action = action;
        this.delay = delay;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        plugin.getScheduler().runTaskLater(delay, () -> action.dispatch(context));
        return FunctionResponse.NONE;
    }

}
