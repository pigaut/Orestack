package io.github.pigaut.rpg.module.function.condition.player.tool;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerToolLoreEquals implements ToolCondition {

    private final List<String> lore;

    public PlayerToolLoreEquals(List<String> lore) {
        this.lore = lore;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        if (!tool.hasItemMeta()) {
            return false;
        }

        ItemMeta toolMeta = tool.getItemMeta();
        if (!toolMeta.hasLore()) {
            return false;
        }

        List<String> lore = toolMeta.getLore();
        if (this.lore.size() != lore.size()) {
            return false;
        }

        for (int i = 0; i < lore.size(); i++) {
            String loreLine = this.lore.get(i);
            String loreLineToCheck = ChatColor.stripColor(lore.get(i));
            if (!loreLine.equals(loreLineToCheck)) {
                return false;
            }
        }
        return true;
    }

}
