package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class LazyFunction implements Function {

    private final EnhancedPlugin plugin;
    private final String name;

    private Function function = null;

    public LazyFunction(@NotNull EnhancedPlugin plugin, @NotNull String name) {
        this.plugin = plugin;
        this.name = name;
    }

    private void lookupFunction() {
        if (function == null && plugin.isReady()) {
            function = plugin.getGlobalFunction(name);
        }
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        lookupFunction();
        if (function == null) {
            Player player = context.player();
            if (player != null) {
                plugin.sendMessage(player, context.withPlaceholder("function-name", name), "function-not-found");
            }
            return FunctionResponse.STOP;
        }
        return function.dispatch(context);
    }


}
