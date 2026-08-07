package io.github.pigaut.rpg.core.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    private ItemBuilder(@NotNull ItemStack item, @NotNull ItemMeta meta) {
        this.item = item;
        this.meta = meta;
    }

    public static ItemBuilder of(@NotNull Material material) {
        Preconditions.checkArgument(MaterialUtil.isNotAir(material), "Material cannot be air");
        ItemStack item = new ItemStack(material);
        return new ItemBuilder(item, item.getItemMeta());
    }

    public static ItemBuilder fromItem(@NotNull ItemStack item) {
        Preconditions.checkArgument(ItemUtil.isNotAir(item), "Item cannot be air");
        return new ItemBuilder(item, item.getItemMeta().clone());
    }

    public static ItemBuilder fromMeta(@NotNull ItemMeta meta) {
        ItemStack item = new ItemStack(Material.DIRT);
        return new ItemBuilder(item, meta.clone());
    }



    public @NotNull ItemMeta buildMeta() {
        return meta.clone();
    }

    public @NotNull ItemStack buildItem() {
        item.setItemMeta(meta);
        return item;
    }

}
