package io.github.pigaut.rpg.module.function.action.item;

import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface ItemAction {

    void execute(@NotNull Player player, @NotNull ItemStack item);

}
