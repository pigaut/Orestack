package io.github.pigaut.rpg.module.item.settings;

import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.stat.*;
import org.bukkit.block.*;
import org.bukkit.enchantments.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface ItemSettings {

    boolean isItemCategory(@NotNull String category);
    @NotNull Set<String> getItemCategories();

    boolean isItemRarity(@NotNull String rarity);
    @NotNull Set<String> getItemRarities();

    int getDefaultBreakingPower();
    @Nullable String getDefaultItemCategory();
    @Nullable String getDefaultItemRarity();
    @Nullable String getDefaultItemName();
    @Nullable String getItemNameByRarity(@NotNull String rarity);
    @NotNull String getDefaultDescriptionColor();
    @Nullable List<String> getDefaultItemLore();

    @NotNull List<String> getItemBreakingPowerLore(@NotNull String breakingPower);
    @NotNull List<String> getItemCategoryLore();
    @NotNull List<String> getItemDescriptionLore();
    @NotNull List<String> getItemCraftedByLore();
    @NotNull List<String> getItemRarityLore(@NotNull String rarity);
    @NotNull List<String> getItemAbilitiesLore();
    @NotNull List<String> getItemStatsLore();
    @NotNull List<String> getItemEnchantsLore();

    boolean isAbilityTemplate(@NotNull String name);
    @Nullable String getAbilityLoreDivider();
    @NotNull Set<String> getAbilityTemplateNames();
    @Nullable List<String> getAbilityTemplate(@NotNull String name);

    boolean isBreakingPower();
    int getPlayerBreakingPower();
    @NotNull Set<BreakingPower> getBreakingPowers();
    @Nullable BlockBreakingPower getBlockBreakingPower(@NotNull Block block);

    int getItemLoreMaxStats();
    @Nullable String getItemLoreStatDivider();
    @NotNull List<String> getItemStatDescription(@NotNull Stat stat);

    int getItemLoreMaxEnchants();
    @Nullable String getItemLoreEnchantDivider();
    @NotNull List<String> getItemEnchantDescription(@NotNull Enchantment enchant);

}
