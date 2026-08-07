package io.github.pigaut.rpg.module.function;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class FunctionManager extends ConfigBackedManager<Function> {

    public FunctionManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.FUNCTIONS, Function.class);
    }

}
