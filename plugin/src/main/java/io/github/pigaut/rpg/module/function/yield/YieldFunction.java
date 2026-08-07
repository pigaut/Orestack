package io.github.pigaut.rpg.module.function.yield;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.Function;
import org.jetbrains.annotations.*;

public interface YieldFunction<R> extends Function {

    @Nullable R yield(@NotNull Context context);

}
