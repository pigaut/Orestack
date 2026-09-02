package io.github.pigaut.rpg.module.function.condition.item.slot;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemIdEquals implements ItemPredicate {

    private final NamespacedKey itemKey;
    private final String[] itemIds;

    public ItemIdEquals(@NotNull EnhancedPlugin plugin, @NotNull Collection<String> itemIds) {
        this.itemKey = plugin.getNamespacedKey("item");
        this.itemIds = itemIds.toArray(new String[0]);
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        for (String itemId : itemIds) {
            if (PersistentData.hasString(meta, itemKey, itemId)) {
                return true;
            }
        }
        return false;
    }

}
