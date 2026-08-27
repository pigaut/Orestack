package io.github.pigaut.rpg.module.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemManager extends ConfigBackedManager<ItemTemplate> {

    private final NamespacedKey itemKey;
    private final NamespacedKey usesKey;
    private final NamespacedKey renamedKey;

    public ItemManager(EnhancedJavaPlugin plugin) {
        super(plugin, Module.ITEMS, ItemTemplate.class);

        itemKey = plugin.getNamespacedKey("item");
        usesKey = plugin.getNamespacedKey("uses");
        renamedKey = plugin.getNamespacedKey("renamed");
    }

    @Override
    public void enable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item == null) {
                    continue;
                }
                ItemTemplate itemTemplate = get(item);
                if (itemTemplate == null) {
                    continue;
                }

                ItemStack updatedItem = itemTemplate.createItemStack(player);
                updatedItem.setAmount(item.getAmount());
                inventory.setItem(i, updatedItem);
            }
        }
    }

    public @NotNull NamespacedKey getItemKey() {
        return itemKey;
    }

    public @NotNull NamespacedKey getUsesKey() {
        return usesKey;
    }

    public @NotNull NamespacedKey getRenamedKey() {
        return renamedKey;
    }

    public @NotNull NamespacedKey getStatKey(@NotNull Stat stat) {
        return plugin.getNamespacedKey(stat.getName());
    }

    public @Nullable ItemStack createItemStack(@NotNull String name) {
        return createItemStack(name, null);
    }

    public @Nullable ItemStack createItemStack(@NotNull String name, @Nullable Player player) {
        ItemTemplate itemTemplate = get(name);
        return itemTemplate != null ? itemTemplate.createItemStack(player) : null;
    }

    public @Nullable ItemTemplate get(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }
        String itemName = PersistentData.getString(item.getItemMeta(), itemKey);
        return itemName != null ? get(itemName) : null;
    }

    public @Nullable List<String> getDescription(@NotNull ItemStack item) {
        ItemTemplate template = get(item);
        return template != null ? template.getDescription() : null;
    }

    public @Nullable List<String> getAbilitiesDescription(@NotNull ItemStack item) {
        ItemTemplate template = get(item);
        return template != null ? template.getAbilitiesDescription() : null;
    }

    public @Nullable String getRarity(@NotNull ItemStack item) {
        ItemTemplate template = get(item);
        return template != null ? template.getRarity() : null;
    }

    public @Nullable Integer getUsesLeft(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }
        return PersistentData.getInteger(item.getItemMeta(), usesKey);
    }

    public void setUses(@NotNull ItemStack item, int uses) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentData.setInteger(meta, usesKey, uses);
            item.setItemMeta(meta);
        }
    }

    public @NotNull Map<Stat, Integer> getStats(@NotNull ItemStack item) {
        Map<Stat, Integer> stats = new HashMap<>();
        for (Stat stat : plugin.getStats().getAll()) {
            Integer statLevel = getStatLevel(item, stat);
            if (statLevel != null) {
                stats.put(stat, statLevel);
            }
        }
        return stats;
    }

    public boolean hasStat(@NotNull ItemStack item, @NotNull Stat statType) {
        if (!item.hasItemMeta()) {
            return false;
        }
        NamespacedKey statKey = getStatKey(statType);
        return PersistentData.hasInteger(item.getItemMeta(), statKey);
    }

    public @Nullable Integer getStatLevel(@NotNull ItemStack item, @NotNull Stat statType) {
        if (!item.hasItemMeta()) {
            return null;
        }

        NamespacedKey statKey = getStatKey(statType);
        return PersistentData.getInteger(item.getItemMeta(), statKey);
    }

    public void setStatLevel(@NotNull ItemStack item, @NotNull Stat statType, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey statKey = getStatKey(statType);
            PersistentData.setInteger(meta, statKey, level);
            item.setItemMeta(meta);
        }
    }

    public @Nullable Integer getStatTotalLevel(@NotNull ItemStack item, @NotNull Stat statType) {
        if (!item.hasItemMeta()) {
            return null;
        }

        Integer statLevel = getStatLevel(item, statType);
        if (statLevel == null) {
            return null;
        }

        Settings settings = plugin.getSettings();
        if (statType == BaseStats.DAMAGE && settings.isSharpnessEnchantAsStat()) {
            double enchantDamageMultiplier = settings.getDamageMultiplierFromSharpnessEnchant(item);
            return (int) (statLevel * enchantDamageMultiplier);
        }

        if (statType == BaseStats.MINING_FORTUNE && settings.isFortuneEnchantAsStat()) {
            int enchantMiningFortune = settings.getMiningFortuneFromFortuneEnchant(item);
            return statLevel + enchantMiningFortune;
        }

        if (statType == BaseStats.MINING_SPEED && settings.isEfficiencyEnchantAsStat()) {
            int enchantMiningSpeed = settings.getMiningSpeedFromEfficiencyEnchant(item);
            return statLevel + enchantMiningSpeed;
        }

        return statLevel;
    }

}