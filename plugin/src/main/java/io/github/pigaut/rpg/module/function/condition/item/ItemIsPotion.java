package io.github.pigaut.rpg.module.function.condition.item;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class ItemIsPotion implements Condition {

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        ItemStack item = context.item();
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta() instanceof PotionMeta;
    }

}
