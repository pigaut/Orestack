package io.github.pigaut.rpg.module.function.condition.item.slot;

import io.github.pigaut.rpg.plugin.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemHasUsesLeft implements ItemPredicate {

    private final EnhancedPlugin plugin;
    private final int minUses;

    public ItemHasUsesLeft(EnhancedPlugin plugin, int minUses) {
        this.plugin = plugin;
        this.minUses = minUses;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        Integer usesLeft = plugin.getItems().getUsesLeft(item);
        if (usesLeft == null) {
            return false;
        }
        return usesLeft >= minUses;
    }

}
