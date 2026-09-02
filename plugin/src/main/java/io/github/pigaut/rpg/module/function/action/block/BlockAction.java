package io.github.pigaut.rpg.module.function.action.block;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface BlockAction extends Action {

    @NotNull
    FunctionResponse dispatch(@NotNull Block block);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return new FunctionError("Event that triggered the function does not have a block");
        }
        return dispatch(block);
    }

    interface Executor extends BlockAction {

        void execute(@NotNull Block block);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull Block block) {
            execute(block);
            return FunctionResponse.NONE;
        }

    }

}
