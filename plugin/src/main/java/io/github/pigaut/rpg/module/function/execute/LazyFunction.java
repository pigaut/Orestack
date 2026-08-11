package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class LazyFunction implements Function {

    private final EnhancedPlugin plugin;

    private final String name;
    private Function function = null;

    public LazyFunction(EnhancedPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    private void lookupFunction() {
        if (function == null && plugin.isReady()) {
            function = plugin.getFunction(name);
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

    @Override
    public @NotNull String getName() {
        lookupFunction();
        return function != null ? function.getName() : StringUtil.randomName();
    }

    @Override
    public @Nullable String getGroup() {
        lookupFunction();
        return function != null ? function.getGroup() : StringUtil.randomName();
    }

}
