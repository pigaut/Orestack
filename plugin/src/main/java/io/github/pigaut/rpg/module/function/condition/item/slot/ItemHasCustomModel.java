package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemHasCustomModel implements ItemPredicate {

    private final Integer[] validModels;

    public ItemHasCustomModel(@NotNull Collection<Integer> validModels) {
        this.validModels = validModels.toArray(new Integer[0]);
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        int toolModel = item.getItemMeta().getCustomModelData();
        for (Integer validModel : validModels) {
            if (toolModel == validModel) {
                return true;
            }
        }
        return false;
    }

}
