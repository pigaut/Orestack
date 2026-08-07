package io.github.pigaut.rpg.core.menu;

import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.core.menu.paged.*;
import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.core.menu.paged.*;
import org.jetbrains.annotations.*;

public class MenuBuilder {

    public static @NotNull FixedMenuBuilder fixed() {
        return new FixedMenuBuilder();
    }

    public static @NotNull FixedMenuBuilder fixed(@NotNull String name, @Nullable String group) {
        return new FixedMenuBuilder(name, group);
    }

    public static @NotNull PagedMenuBuilder paged() {
        return new PagedMenuBuilder();
    }

    public static @NotNull PagedMenuBuilder paged(@NotNull String name, @Nullable String group) {
        return new PagedMenuBuilder(name, group);
    }

    public static @NotNull AtlasMenuBuilder atlas() {
        return new AtlasMenuBuilder();
    }

    public static @NotNull AtlasMenuBuilder atlas(@NotNull String name, @Nullable String group) {
        return new AtlasMenuBuilder(name, group);
    }

}
