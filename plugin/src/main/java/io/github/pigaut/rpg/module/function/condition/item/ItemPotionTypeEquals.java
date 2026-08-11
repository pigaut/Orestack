package io.github.pigaut.rpg.module.function.condition.item;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;

public class ItemPotionTypeEquals implements Condition {

    private final PotionType requiredPotion;

    public ItemPotionTypeEquals(PotionType requiredPotion) {
        this.requiredPotion = requiredPotion;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        ItemStack item = context.item();
        if (item == null || !item.hasItemMeta()) {
            return null;
        }

        if (!(item.getItemMeta() instanceof PotionMeta potionMeta)) {
            return null;
        }

        PotionType potionType = potionMeta.getBasePotionType();
        return potionType == requiredPotion;
    }

}
