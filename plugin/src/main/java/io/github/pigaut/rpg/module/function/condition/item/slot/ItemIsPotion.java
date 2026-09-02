package io.github.pigaut.rpg.module.function.condition.item.slot;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class ItemIsPotion implements ItemPredicate {

    @Override
    public boolean test(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta() instanceof PotionMeta;
    }

}
