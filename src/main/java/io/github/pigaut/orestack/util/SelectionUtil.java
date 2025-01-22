package io.github.pigaut.orestack.util;

import io.github.pigaut.orestack.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.*;

import java.util.*;

public class SelectionUtil {

    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    private SelectionUtil() {}

    public static final NamespacedKey WAND_KEY = new NamespacedKey("orestack", "wand");

    public static boolean isSelectionWand(ItemStack item) {
        if (item == null) {
            return false;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        return meta.getPersistentDataContainer().has(WAND_KEY, PersistentDataType.STRING);
    }

    public static ItemStack getSelectionWand() {
        final ItemStack wandItem = new ItemStack(Material.GOLDEN_PICKAXE);
        final ItemMeta meta = wandItem.getItemMeta();

        final String name = plugin.getLang("wand-item-name", "Generator Wand");
        final List<String> lore = new ArrayList<>();
        lore.add(plugin.getLang("wand-item-left-click", ChatColor.WHITE + "left-click to mark selection"));

        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addEnchant(Enchantment.OXYGEN, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(WAND_KEY, PersistentDataType.STRING, "true");

        wandItem.setItemMeta(meta);
        return wandItem;
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
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    locations.add(new Location(world, x, y, z));
                }
            }
        }
        return locations;
    }

}
