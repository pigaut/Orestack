package io.github.pigaut.rpg.core.tool;

import io.github.pigaut.rpg.plugin.registry.*;
import io.github.pigaut.rpg.plugin.registry.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ToolRegistry extends AbstractRegistry<Tool> {

    public @Nullable Tool get(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        for (NamespacedKey namespaceKey : meta.getPersistentDataContainer().getKeys()) {
            Tool tool = get(namespaceKey.getKey());
            if (tool != null) {
                return tool;
            }
        }
        return null;
    }

}
