package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.mob.*;
import org.jetbrains.annotations.*;

public interface MobAction extends Action {

    @NotNull
    FunctionResponse dispatch(@NotNull Mob mob);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Mob mob = context.mob();
        if (mob == null) {
            return new FunctionError("Event that triggered the function does not have a mob");
        }
        return dispatch(mob);
    }

    interface Executor extends MobAction {

        void execute(@NotNull Mob mob);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull Mob mob) {
            execute(mob);
            return FunctionResponse.NONE;
        }

    }

}
