package io.github.pigaut.rpg.module.function.condition.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.Condition;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public interface GeneratorCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull Generator generator);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return new FunctionError("Event that triggered the function does not have a block");
        }

        RpgMakerPlugin plugin = RpgMakerPlugin.getInstance();
        Generator generator = plugin.getGenerator(context.player(), block.getLocation());
        if (generator == null) {
            return new FunctionError("Event that triggered the function does not have a generator");
        }

        return evaluate(generator);
    }

    @FunctionalInterface
    interface Predicate extends GeneratorCondition {

        boolean test(@NotNull Generator generator);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull Generator generator) {
            return test(generator) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}
