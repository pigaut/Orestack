package io.github.pigaut.orestack.util;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.placeholder.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class GeneratorTool {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    public static final NamespacedKey GENERATOR_KEY = new NamespacedKey("orestack", "generator");
    private static final ItemStack DEFAULT_ITEM;

    static {
        DEFAULT_ITEM = IconBuilder.of(Material.TERRACOTTA)
                .enchanted(true)
                .withDisplay("&b&l{generator_tc} Generator ({generator_rotation_tc})")
                .addLore("&fright-click to place")
                .addLore("&fleft-click to break")
                .addLore("&fshift + left-click (air) to rotate")
                .buildIcon();
    }

    public static @NotNull ItemStack getDefaultItem() {
        return DEFAULT_ITEM.clone();
    }

    public static @NotNull Rotation getToolRotation(@NotNull ItemStack item) {
        final GeneratorTemplate generator = getGeneratorFromTool(item);
        return generator != null ? generator.getRotation() : Rotation.NONE;
    }

    public static void incrementToolRotation(@NotNull ItemStack item) {
        final GeneratorTemplate generator = getGeneratorFromTool(item);
        if (generator == null) {
            return;
        }
        generator.setRotation(generator.getRotation().next());
        final ItemMeta templateMeta = plugin.getTools().getGeneratorTool().getItemMeta();
        if (templateMeta != null) {
            PersistentData.setString(templateMeta, GENERATOR_KEY, generator.getName());
            item.setItemMeta(templateMeta);
        }

        ItemPlaceholders.parseAll(item, generator);
    }

    public static @Nullable GeneratorTemplate getGeneratorFromTool(@Nullable ItemStack item) {
        final String generatorName = getGeneratorNameFromTool(item);
        if (generatorName == null) {
            return null;
        }
        return plugin.getGeneratorTemplate(generatorName);
    }

    public static @Nullable String getGeneratorNameFromTool(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        return PersistentData.getString(meta, GENERATOR_KEY);
    }

    public static @NotNull ItemStack getGeneratorTool(@NotNull GeneratorTemplate generator) {
        final ItemStack generatorItem = plugin.getTools().getGeneratorTool();

        final Material material = generator.getItemType();
        generatorItem.setType(Crops.isCrop(material) ? Crops.getCropSeeds(material) : material);

        final ItemMeta meta = generatorItem.getItemMeta();
        if (meta != null) {
            PersistentData.setString(meta, GENERATOR_KEY, generator.getName());
            generatorItem.setItemMeta(meta);
        }

        return ItemPlaceholders.parseAll(generatorItem, generator);
    }

}
