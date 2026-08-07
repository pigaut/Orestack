package io.github.pigaut.rpg.module.function.foreach;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface ForEachSource<T> {

    @NotNull Collection<T> getElements(@NotNull Context context);

}
