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
        System.out.println("Debug 1");
        if (function == null) {
            Player player = context.player();
            System.out.println("Debug 2");
            if (player != null) {
                System.out.println("Debug 3");
                plugin.sendMessage(player, context.withPlaceholder("function-name", name), "function-not-found");
            }
            System.out.println("Debug 4");
            return FunctionResponse.STOP;
        }
        System.out.println("Debug 5");
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
