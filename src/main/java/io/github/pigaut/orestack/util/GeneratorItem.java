package io.github.pigaut.orestack.util;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.meta.placeholder.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorItem {

    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();
    public static final NamespacedKey GENERATOR_KEY = new NamespacedKey("orestack", "generator");

    public static @Nullable String getGeneratorFromItem(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        final String generatorName = meta.getPersistentDataContainer().get(GENERATOR_KEY, PersistentDataType.STRING);
        return generatorName;
    }

    public static @NotNull ItemStack getItemFromGenerator(@NotNull GeneratorTemplate generator) {
        final Material itemType = generator.getItemType();
        final ItemStack generatorItem = new ItemStack(itemType);
        final ItemMeta meta = generatorItem.getItemMeta();

        final String name = plugin.getLang("generator-item-name", "%Generator% Generator");
        final List<String> lore = new ArrayList<>();
        lore.add(plugin.getLang("generator-item-right-click", ChatColor.WHITE + "right-click to place"));
        lore.add(plugin.getLang("generator-item-left-click", ChatColor.WHITE + "left-click to place"));

        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addEnchant(Enchantment.OXYGEN, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(GENERATOR_KEY, PersistentDataType.STRING, generator.getName());
        generatorItem.setItemMeta(meta);

        return ItemPlaceholders.parseAll(generatorItem, generator);
    }

}
