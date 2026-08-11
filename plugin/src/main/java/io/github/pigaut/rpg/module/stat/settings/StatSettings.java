package io.github.pigaut.rpg.module.stat.settings;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface StatSettings {

    boolean isShowStatusBar();

    boolean isInsertMessagesInStatusBar();

    @NotNull
    Delay getStatusBarMessageDuration();

    @NotNull
    BarAlignment getInsertedMessageAlign();

    int getInsertedMessageLength();

    @NotNull
    String getStatusBar();

    int getBaseDamage();

    int getBaseDefense();

    int getBaseMaxHealth();

    int getBaseHealthRegen();

    int getBaseMaxMana();

    int getBaseManaRegen();

    double getBaseCritDamage();

    double getBaseCritChance();

    int getBaseMiningFortune();

    int getBaseFarmingFortune();

    int getBaseForagingFortune();

    int getBaseMovementSpeed();

    int getBaseAttackSpeed();

    int getBaseMiningSpeed();

    @NotNull
    Map<Stat, CustomStat> getCustomStats();

    @Nullable
    CustomStat getCustomStat(@NotNull Stat statType);

    @NotNull
    Delay getCombatDuration();

    boolean isRegenHealthDuringCombat();

    boolean isRegenManaDuringCombat();

    boolean isSharpnessEnchantAsStat();

    double getDamageMultiplierFromSharpnessEnchant(@NotNull ItemStack item);

    boolean isEfficiencyEnchantAsStat();

    int getMiningSpeedFromEfficiencyEnchant(@NotNull ItemStack item);

    double getEfficiencyMiningSpeedDebuff(@NotNull ItemStack item);

    boolean isFortuneEnchantAsStat();

    int getMiningFortuneFromFortuneEnchant(@NotNull ItemStack item);

    int getHealthRegenInterval();

    int getManaRegenInterval();

}
