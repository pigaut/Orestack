package io.github.pigaut.rpg.module.function.condition.player.tool;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerToolIdEquals implements ToolCondition {

    private final NamespacedKey itemKey;
    private final List<String> itemIds;

    public PlayerToolIdEquals(EnhancedPlugin plugin, List<String> itemIds) {
        this.itemKey = plugin.getNamespacedKey("item");
        this.itemIds = itemIds;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        if (!tool.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = tool.getItemMeta();
        for (String itemId : itemIds) {
            if (PersistentData.hasString(meta, itemKey, itemId)) {
                return true;
            }
        }
        return false;
    }

}
