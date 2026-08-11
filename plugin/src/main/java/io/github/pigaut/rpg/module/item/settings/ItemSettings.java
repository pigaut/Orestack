package io.github.pigaut.rpg.module.item.settings;

import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.stat.*;
import org.bukkit.block.*;
import org.bukkit.enchantments.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface ItemSettings {

    @NotNull List<String> getDefaultItemLore();

    @NotNull String getDefaultItemName();

    @NotNull String getDefaultDescriptionColor();
    @NotNull List<String> getDescriptionHeader();
    @NotNull List<String> getDescriptionFooter();

    int getStatsDescriptionMaxLines();
    @NotNull List<String> getStatsDescriptionHeader();
    @NotNull List<String> getStatsDescriptionDivider();
    @NotNull List<String> getStatsDescriptionFooter();
    @NotNull List<String> getStatDescription(@NotNull Stat statType);

    int getEnchantsDescriptionMaxLines();
    @NotNull List<String> getEnchantsDescriptionHeader();
    @NotNull List<String> getEnchantsDescriptionDivider();
    @NotNull List<String> getEnchantsDescriptionFooter();
    @NotNull List<String> getEnchantDescription(@NotNull Enchantment enchant);

    @NotNull List<String> getAbilityDescriptionHeader();
    @NotNull List<String> getAbilityDescriptionDivider();
    @NotNull List<String> getAbilityDescriptionFooter();
    @NotNull List<String> getAbilityDescriptionTemplate();

    boolean isItemRarity(@NotNull String rarity);
    @Nullable String getDefaultItemRarity();
    @Nullable String getItemRarityDisplay(@NotNull String name);

    @Nullable String getItemNameByRarity(@NotNull String name);

    @NotNull List<String> getItemRarityDescription(@NotNull String name);

    @NotNull List<String> getRarityHeader();
    @NotNull List<String> getRarityFooter();

    boolean isBreakingPower();
    int getDefaultBreakingPower();
    @NotNull Set<BreakingPower> getBreakingPowers();
    @Nullable BlockBreakingPower getBlockBreakingPower(@NotNull Block block);
    @NotNull List<String> getBreakingPowerHeader();
    @NotNull List<String> getBreakingPowerFooter();

}
