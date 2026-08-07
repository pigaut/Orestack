package io.github.pigaut.rpg.core.placeholder;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import org.jetbrains.annotations.*;

public interface PlaceholderResolver {

    @Nullable
    Object resolve(@NotNull Context context);

}
