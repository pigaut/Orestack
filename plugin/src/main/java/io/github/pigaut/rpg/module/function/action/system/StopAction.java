package io.github.pigaut.rpg.module.function.action.system;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class StopAction implements Action {

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return FunctionResponse.STOP;
    }

}
