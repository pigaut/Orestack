package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemEquals implements ItemPredicate {

    private final ItemStack[] validItems;

    public ItemEquals(@NotNull Collection<ItemStack> validItems) {
        this.validItems = validItems.toArray(new ItemStack[0]);
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        for (ItemStack validItem : validItems) {
            if (validItem.equals(item)) {
                return true;
            }
        }
        return false;
    }

}
