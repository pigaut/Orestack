package io.github.pigaut.rpg.module.function.condition.item.slot;

import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemHasUses implements ItemPredicate {

    private final EnhancedPlugin plugin;

    public ItemHasUses(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        ItemTemplate itemTemplate = plugin.getItemTemplate(item);
        if (itemTemplate == null) {
            return false;
        }
        return itemTemplate.hasUses();
    }

}
