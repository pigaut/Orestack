package io.github.pigaut.rpg.module.menu.button;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import org.jetbrains.annotations.*;

public interface ButtonTemplate {

    @NotNull
    Button createButton(@NotNull Context context);

}
