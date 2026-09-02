package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class ItemNameEquals implements ItemPredicate {

    private final String name;

    public ItemNameEquals(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta toolMeta = item.getItemMeta();
        if (!toolMeta.hasDisplayName()) {
            return false;
        }

        String toolName = ChatColor.stripColor(toolMeta.getDisplayName());
        return toolName.equals(name);
    }

}
