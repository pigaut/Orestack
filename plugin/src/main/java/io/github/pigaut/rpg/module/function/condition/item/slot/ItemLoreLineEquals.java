package io.github.pigaut.rpg.module.function.condition.item.slot;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemLoreLineEquals implements ItemPredicate {

    private final String lore;
    private final int line;

    public ItemLoreLineEquals(@NotNull String lore, int line) {
        this.lore = lore;
        this.line = line;
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
        if (line >= lore.size()) {
            return false;
        }

        String loreLine = ChatColor.stripColor(lore.get(line));
        return loreLine.equals(this.lore);
    }

}
