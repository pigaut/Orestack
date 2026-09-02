package io.github.pigaut.rpg.module.function.condition.block;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface BlockCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull Block block);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return new FunctionError("Event that triggered the function does not have a block");
        }
        return evaluate(block);
    }

    @FunctionalInterface
    interface Predicate extends BlockCondition {

        boolean test(@NotNull Block block);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull Block block) {
            return test(block) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}