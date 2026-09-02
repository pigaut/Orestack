package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemLoreEquals implements ItemPredicate {

    private final List<String> validLore;

    public ItemLoreEquals(@NotNull List<String> validLore) {
        this.validLore = List.copyOf(validLore);
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta toolMeta = item.getItemMeta();
        if (!toolMeta.hasLore()) {
            return false;
        }

        List<String> lore = toolMeta.getLore();
        if (this.validLore.size() != lore.size()) {
            return false;
        }

        for (int i = 0; i < lore.size(); i++) {
            String loreLine = this.validLore.get(i);
            String loreLineToCheck = ChatColor.stripColor(lore.get(i));
            if (!loreLine.equals(loreLineToCheck)) {
                return false;
            }
        }
        return true;
    }

}
