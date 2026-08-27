package io.github.pigaut.rpg.module.stat;

import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StatsMap {

    private final Map<Stat, PlayerStat> baseStats = new HashMap<>();
    private final Map<Stat, CustomStat> customStats = new HashMap<>();

    public void put(@NotNull Stat type, @NotNull PlayerStat baseStat) {
        baseStats.put(type, baseStat);
    }

    public void put(@NotNull Stat type, @NotNull CustomStat customStat) {
        customStats.put(type, customStat);
    }

    public void putAll(@NotNull Map<Stat, CustomStat> customStats) {
        this.customStats.putAll(customStats);
    }

    public void clear() {
        for (PlayerStat baseStat : baseStats.values()) {
            baseStat.clear();
        }
        baseStats.clear();

        for (CustomStat customStat : customStats.values()) {
            customStat.clear();
        }
        customStats.clear();
    }

    private int getTotal(@NotNull Stat stat, @Nullable PlayerStat baseStat) {
        if (baseStat == null) {
            return 0;
        }

        double base = baseStat.getTotal();

        Map<LeveledStatModifier, Integer> modifierLevels = new HashMap<>();
        for (CustomStat customStat : customStats.values()) {
            LeveledStatModifier modifier = customStat.getStatModifier(stat);
            if (modifier != null) {
                modifierLevels.put(modifier, (int) customStat.getTotal());
            }
        }

        return (int) StatUtil.calculateStatTotal(base, modifierLevels);
    }

    public @NotNull PlayerStat getBaseMaxHealth() {
        return baseStats.get(BaseStats.MAX_HEALTH);
    }

    public int getTotalMaxHealth() {
        return getTotal(BaseStats.MAX_HEALTH, baseStats.get(BaseStats.MAX_HEALTH));
    }

    public @NotNull PlayerStat getBaseHealthRegen() {
        return baseStats.get(BaseStats.HEALTH_REGEN);
    }

    public int getTotalHealthRegen() {
        return getTotal(BaseStats.HEALTH_REGEN, baseStats.get(BaseStats.HEALTH_REGEN));
    }

    public @NotNull PlayerStat getBaseDefense() {
        return baseStats.get(BaseStats.DEFENSE);
    }

    public int getTotalDefense() {
        return getTotal(BaseStats.DEFENSE, baseStats.get(BaseStats.DEFENSE));
    }

    public @NotNull PlayerStat getBaseMovementSpeed() {
        return baseStats.get(BaseStats.MOVEMENT_SPEED);
    }

    public int getTotalMovementSpeed() {
        return getTotal(BaseStats.MOVEMENT_SPEED, baseStats.get(BaseStats.MOVEMENT_SPEED));
    }

    public @NotNull PlayerStat getBaseAttackSpeed() {
        return baseStats.get(BaseStats.ATTACK_SPEED);
    }

    public int getTotalAttackSpeed() {
        return getTotal(BaseStats.ATTACK_SPEED, baseStats.get(BaseStats.ATTACK_SPEED));
    }

    public @NotNull PlayerStat getBaseMaxMana() {
        return baseStats.get(BaseStats.MAX_MANA);
    }

    public int getTotalMaxMana() {
        return getTotal(BaseStats.MAX_MANA, baseStats.get(BaseStats.MAX_MANA));
    }

    public @NotNull PlayerStat getBaseManaRegen() {
        return baseStats.get(BaseStats.MANA_REGEN);
    }

    public int getTotalManaRegen() {
        return getTotal(BaseStats.MANA_REGEN, baseStats.get(BaseStats.MANA_REGEN));
    }

    public @NotNull PlayerStat getBaseDamage() {
        return baseStats.get(BaseStats.DAMAGE);
    }

    public int getTotalDamage() {
        return getTotal(BaseStats.DAMAGE, baseStats.get(BaseStats.DAMAGE));
    }

    public @NotNull PlayerStat getBaseCritDamage() {
        return baseStats.get(BaseStats.CRIT_DAMAGE);
    }

    public int getTotalCritDamage() {
        return getTotal(BaseStats.CRIT_DAMAGE, baseStats.get(BaseStats.CRIT_DAMAGE));
    }

    public @NotNull PlayerStat getBaseCritChance() {
        return baseStats.get(BaseStats.CRIT_CHANCE);
    }

    public int getTotalCritChance() {
        return getTotal(BaseStats.CRIT_CHANCE, baseStats.get(BaseStats.CRIT_CHANCE));
    }

    public @NotNull PlayerStat getBaseForagingFortune() {
        return baseStats.get(BaseStats.FORAGING_FORTUNE);
    }

    public int getTotalForagingFortune() {
        return getTotal(BaseStats.FORAGING_FORTUNE, baseStats.get(BaseStats.FORAGING_FORTUNE));
    }

    public @NotNull PlayerStat getBaseFarmingFortune() {
        return baseStats.get(BaseStats.FARMING_FORTUNE);
    }

    public int getTotalFarmingFortune() {
        return getTotal(BaseStats.FARMING_FORTUNE, baseStats.get(BaseStats.FARMING_FORTUNE));
    }

    public @NotNull PlayerStat getBaseMiningFortune() {
        return baseStats.get(BaseStats.MINING_FORTUNE);
    }

    public int getTotalMiningFortune() {
        return getTotal(BaseStats.MINING_FORTUNE, baseStats.get(BaseStats.MINING_FORTUNE));
    }

    public @Nullable PlayerStat getBaseMiningSpeed() {
        return baseStats.get(BaseStats.MINING_SPEED);
    }

    public int getTotalMiningSpeed() {
        return getTotal(BaseStats.MINING_SPEED, baseStats.get(BaseStats.MINING_SPEED));
    }

    public @Nullable PlayerStat get(@NotNull Stat statType) {
        return baseStats.getOrDefault(statType, customStats.get(statType));
    }

    public @NotNull List<PlayerStat> getAll() {
        List<PlayerStat> stats = new ArrayList<>(baseStats.values());
        stats.addAll(customStats.values());
        return stats;
    }

}