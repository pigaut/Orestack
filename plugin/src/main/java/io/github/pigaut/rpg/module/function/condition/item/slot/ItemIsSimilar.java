package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemIsSimilar implements ItemPredicate {

    private final ItemStack[] validItems;

    public ItemIsSimilar(@NotNull Collection<ItemStack> validItems) {
        this.validItems = validItems.toArray(new ItemStack[0]);
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        for (ItemStack validItem : validItems) {
            if (validItem.isSimilar(item)) {
                return true;
            }
        }
        return false;
    }

}
