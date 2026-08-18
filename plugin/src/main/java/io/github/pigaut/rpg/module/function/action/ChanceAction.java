package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.util.*;
import org.jetbrains.annotations.*;

public class ChanceAction implements Action {

    private final Action action;
    private final double chance;

    public ChanceAction(Action action, double chance) {
        this.action = action;
        this.chance = chance;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        if (Probability.test(chance)) {
            return action.dispatch(context);
        }
        return FunctionResponse.NONE;
    }

}
