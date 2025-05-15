package io.github.pigaut.orestack.util;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.placeholder.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorTools {

    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public static final NamespacedKey GENERATOR_KEY = new NamespacedKey("orestack", "generator");
    public static final NamespacedKey WAND_KEY = new NamespacedKey("orestack", "wand");

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
        if (item == null) {
            return null;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        final String generatorName = PersistentData.getString(meta, GENERATOR_KEY);
        return plugin.getGeneratorTemplate(generatorName);
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

    public static boolean isWandTool(@Nullable ItemStack item) {
        if (item == null) {
            return false;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        return PersistentData.hasString(meta, WAND_KEY);
    }

    public static ItemStack getWandTool() {
        final ItemStack wandItem = plugin.getTools().getWandTool();

        final ItemMeta meta = wandItem.getItemMeta();
        if (meta != null) {
            PersistentData.setString(meta, WAND_KEY, "true");
            wandItem.setItemMeta(meta);
        }

        return ItemPlaceholders.parseAll(wandItem);
    }

    public static List<Location> getSelectedRegion(World world, Location firstPoint, Location secondPoint) {
        return getSelectedRegion(world,
                firstPoint.getBlockX(), firstPoint.getBlockY(), firstPoint.getBlockZ(),
                secondPoint.getBlockX(), secondPoint.getBlockY(), secondPoint.getBlockZ());
    }

    public static List<Location> getSelectedRegion(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        final List<Location> locations = new ArrayList<>();
        for (int x = maxX; x >= minX; x--) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = maxZ; z >= minZ; z--) {
                    locations.add(new Location(world, x, y, z));
                }
            }
        }
        return locations;
    }

}
