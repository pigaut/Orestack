package io.github.pigaut.rpg.module.function.condition.player.tool;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class PlayerToolNameEquals implements ToolCondition {

    private final String name;

    public PlayerToolNameEquals(String name) {
        this.name = name;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        if (!tool.hasItemMeta()) {
            return false;
        }

        ItemMeta toolMeta = tool.getItemMeta();
        if (!toolMeta.hasDisplayName()) {
            return false;
        }

        String toolName = ChatColor.stripColor(toolMeta.getDisplayName());
        return toolName.equals(name);
    }

}
