package io.github.pigaut.rpg.plugin.manager;

import io.github.pigaut.rpg.plugin.registry.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public interface Identifiable {

    @NotNull String getName();

    @Nullable String getGroup();

    @NotNull default ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.TERRACOTTA);
        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(this.getName());
        }
        return icon;
    }

}
