package io.github.pigaut.orestack.util;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.placeholder.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class GeneratorTool {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    private static final NamespacedKey GENERATOR_TOOL_KEY = plugin.getNamespacedKey("generator_tool");
    private static final NamespacedKey GENERATOR_NAME_KEY = plugin.getNamespacedKey("generator_name");
    private static final NamespacedKey GENERATOR_ROTATION_KEY = plugin.getNamespacedKey("generator_rotation");
    private static final ItemStack ITEM_TEMPLATE;

    static {
        ITEM_TEMPLATE = IconBuilder.of(Material.TERRACOTTA)
                .enchanted(true)
                .withDisplay("&b&l{generator_tc} Generator ({generator_rotation_tc})")
                .addLore("&fright-click to place")
                .addLore("&fleft-click to break")
                .addLore("&fshift + left-click (air) to rotate")
                .buildIcon();
    }

    public static @NotNull ItemStack getItemTemplate() {
        return ITEM_TEMPLATE.clone();
    }

    public static @NotNull ItemStack createItem(@NotNull GeneratorTemplate generator) {
        ItemStack generatorItem = plugin.getSettings().getGeneratorTool();

        Material itemType = generator.getItemType();
        if (MaterialUtil.isAir(itemType)) {
            generatorItem.setType(Material.TERRACOTTA);
        }
        else if (MaterialUtil.isCrop(itemType)) {
            generatorItem.setType(MaterialUtil.getCropSeeds(itemType));
        }
        else {
            generatorItem.setType(itemType);
        }

        ItemMeta meta = generatorItem.getItemMeta();
        updateToolData(meta, generator, "NONE");
        generatorItem.setItemMeta(meta);

        Placeholder rotationPlaceholder = Placeholder.of("{generator_tool_rotation}", "NONE");
        return ItemPlaceholders.parseAll(generatorItem, generator, rotationPlaceholder);
    }

    public static boolean isValidItem(@NotNull ItemStack item) {
        return item.hasItemMeta() && PersistentData.hasTag(item.getItemMeta(), GENERATOR_TOOL_KEY);
    }

    public static @Nullable GeneratorTemplate getGeneratorTemplate(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }
        String generatorName = PersistentData.getString(item.getItemMeta(), GENERATOR_NAME_KEY);
        return generatorName != null ? plugin.getGeneratorTemplate(generatorName) : null;
    }

    public static @Nullable String getRotationData(@NotNull ItemStack item) {
        return item.hasItemMeta() ? PersistentData.getString(item.getItemMeta(), GENERATOR_ROTATION_KEY) : null;
    }

    public static @Nullable Rotation getRotation(@NotNull ItemStack item) {
        String rotationName = getRotationData(item);
        if (rotationName == null) {
            return null;
        }
        if (rotationName.equalsIgnoreCase("RANDOM")) {
            return Rotation.random();
        }
        return ParseUtil.parseEnumOrNull(Rotation.class, rotationName);
    }

    public static void switchToolRotation(@NotNull ItemStack item) {
        Preconditions.checkArgument(isValidItem(item), "Item is not a generator tool");

        String currentRotationName = getRotationData(item);
        String newRotation = "NONE";
        if (currentRotationName != null) {
            switch (currentRotationName) {
                case "NONE" -> newRotation = "RIGHT";
                case "RIGHT" -> newRotation = "BACK";
                case "BACK" -> newRotation = "LEFT";
                case "LEFT" -> newRotation = "RANDOM";
                case "RANDOM" -> newRotation = "NONE";
            }
        }

        ItemMeta meta = plugin.getSettings().getGeneratorTool().getItemMeta();
        GeneratorTemplate generator = getGeneratorTemplate(item);
        updateToolData(meta, generator, newRotation);
        item.setItemMeta(meta);

        Placeholder rotationPlaceholder = Placeholder.of("{generator_tool_rotation}", newRotation.toString());
        ItemPlaceholders.parseAll(item, generator, rotationPlaceholder);
    }

    private static void updateToolData(@NotNull ItemMeta meta, @NotNull GeneratorTemplate generator, @NotNull String rotationData) {
        PersistentData.setTag(meta, GENERATOR_TOOL_KEY);
        PersistentData.setString(meta, GENERATOR_NAME_KEY, generator.getName());
        PersistentData.setString(meta, GENERATOR_ROTATION_KEY, rotationData);
    }

}
