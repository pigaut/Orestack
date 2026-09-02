package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;

public class ItemPotionTypeEquals implements ItemPredicate {

    private final PotionType validPotion;

    public ItemPotionTypeEquals(@NotNull PotionType validPotion) {
        this.validPotion = validPotion;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        if (!(item.getItemMeta() instanceof PotionMeta potionMeta)) {
            return false;
        }

        PotionType potionType = potionMeta.getBasePotionType();
        return potionType == validPotion;
    }

}
