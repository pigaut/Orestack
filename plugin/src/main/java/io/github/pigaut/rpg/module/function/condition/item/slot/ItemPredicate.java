package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public interface ItemPredicate {

    boolean test(@NotNull ItemStack item);

}
