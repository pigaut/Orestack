package io.github.pigaut.rpg.module.function.action.protagonist;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface ProtagonistAction extends Action {

    @NotNull
    FunctionResponse dispatch(@NotNull LivingEntity protagonist);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        LivingEntity protagonist = context.protagonist();
        if (protagonist == null) {
            return new FunctionError("Event that triggered the function does not have a protagonist");
        }
        return dispatch(protagonist);
    }

    interface Executor extends ProtagonistAction {

        void execute(@NotNull LivingEntity protagonist);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull LivingEntity protagonist) {
            execute(protagonist);
            return FunctionResponse.NONE;
        }

    }

}
