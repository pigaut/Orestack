package io.github.pigaut.rpg.module.menu.button.dynamic;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface DynamicIcon {

    void apply(@NotNull ItemStack item, @NotNull Context context);

}
