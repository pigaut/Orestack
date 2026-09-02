package io.github.pigaut.rpg.module.function.action.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.generator.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface GeneratorAction extends Action {

    @NotNull
    FunctionResponse dispatch(@NotNull Generator generator);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return new FunctionError("Event that triggered the function does not have a block");
        }

        RpgMakerPlugin plugin = RpgMakerPlugin.getInstance();
        Generator generator = plugin.getGenerator(context.player(), block.getLocation());
        if (generator == null) {
            return new FunctionError("Block does not have a generator");
        }

        return dispatch(generator);
    }

    interface Executor extends GeneratorAction {

        void execute(@NotNull Generator generator);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull Generator generator) {
            execute(generator);
            return FunctionResponse.NONE;
        }

    }

}
