package io.github.pigaut.orestack.util;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.meta.placeholder.*;
import io.github.pigaut.voxel.util.Rotation;
import io.github.pigaut.voxel.yaml.parser.deserializer.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorItem {

    public static final NamespacedKey GENERATOR_KEY = new NamespacedKey("orestack", "generator");
    public static final NamespacedKey ROTATION_KEY = new NamespacedKey("orestack", "rotation");
    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public static @NotNull Rotation getRotationFromItem(@NotNull ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return Rotation.NONE;
        }
        final String rotationData = meta.getPersistentDataContainer().get(ROTATION_KEY, PersistentDataType.STRING);
        if (rotationData == null) {
            return Rotation.NONE;
        }
        final Rotation rotation = Deserializers.getEnum(Rotation.class, rotationData);
        return rotation != null ? rotation : Rotation.NONE;
    }

    public static void incrementItemRotation(@NotNull ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        final PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        final String rotationData = dataContainer.get(ROTATION_KEY, PersistentDataType.STRING);
        if (rotationData != null) {
            final Rotation currentRotation = Deserializers.getEnum(Rotation.class, rotationData);
            dataContainer.set(ROTATION_KEY, PersistentDataType.STRING, Rotation.getNextRotation(currentRotation).toString());
        } else {
            dataContainer.set(ROTATION_KEY, PersistentDataType.STRING, Rotation.NONE.toString());
        }

        item.setItemMeta(meta);
    }

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
        lore.add(plugin.getLang("generator-item-rotate", ChatColor.WHITE + "shift + left-click to rotate"));

        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addEnchant(Enchantment.OXYGEN, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(GENERATOR_KEY, PersistentDataType.STRING, generator.getName());
        generatorItem.setItemMeta(meta);

        return ItemPlaceholders.parseAll(generatorItem, generator);
    }

}
