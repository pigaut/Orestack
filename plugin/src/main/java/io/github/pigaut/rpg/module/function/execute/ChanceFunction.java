package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.util.*;
import org.jetbrains.annotations.*;

public class ChanceFunction implements Function {

    private final Function function;
    private final double chance;

    public ChanceFunction(@NotNull Function function, double chance) {
        this.function = function;
        this.chance = chance;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return Probability.test(chance) ? function.dispatch(context) : FunctionResponse.NONE;
    }

}
