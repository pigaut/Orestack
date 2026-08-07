package io.github.pigaut.rpg.module.function.condition.player.tool;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerToolLoreLineEquals implements ToolCondition {

    private final String lore;
    private final int line;

    public PlayerToolLoreLineEquals(String lore, int line) {
        this.lore = lore;
        this.line = line;
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
        if (line >= lore.size()) {
            return false;
        }

        String loreLine = ChatColor.stripColor(lore.get(line));
        return loreLine.equals(this.lore);
    }

}
