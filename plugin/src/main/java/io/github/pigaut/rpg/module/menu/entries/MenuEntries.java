package io.github.pigaut.rpg.module.menu.entries;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface MenuEntries {

    @NotNull
    List<Button> createEntries(@NotNull Context context, @Nullable ButtonTemplate buttonTemplate);

}
