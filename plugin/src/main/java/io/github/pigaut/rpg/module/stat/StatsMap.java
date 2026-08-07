package io.github.pigaut.rpg.module.stat;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.module.stat.attribute.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.module.stat.attribute.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.version.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StatsMap {

    private final Map<Stat, PlayerStat> baseStats = new HashMap<>();
    private final Map<Stat, CustomStat> customStats = new HashMap<>();

    public StatsMap(@NotNull EnhancedPlugin plugin, @NotNull PlayerState playerState) {
        Settings settings = plugin.getSettings();

        baseStats.put(BaseStats.MAX_HEALTH, new PlayerStat(settings.getBaseMaxHealth()));
        baseStats.put(BaseStats.MAX_MANA, new PlayerStat(settings.getBaseMaxMana()));
        baseStats.put(BaseStats.DEFENSE, new PlayerStat(settings.getBaseDefense()));
        baseStats.put(BaseStats.DAMAGE, new PlayerStat(settings.getBaseDamage()));
        baseStats.put(BaseStats.HEALTH_REGEN, new PlayerStat(settings.getBaseHealthRegen()));
        baseStats.put(BaseStats.MANA_REGEN, new PlayerStat(settings.getBaseManaRegen()));
        baseStats.put(BaseStats.CRIT_DAMAGE, new PlayerStat(settings.getBaseCritDamage()));
        baseStats.put(BaseStats.CRIT_CHANCE, new PlayerStat(settings.getBaseCritChance()));
        baseStats.put(BaseStats.MINING_FORTUNE, new PlayerStat(settings.getBaseMiningFortune()));
        baseStats.put(BaseStats.FARMING_FORTUNE, new PlayerStat(settings.getBaseFarmingFortune()));
        baseStats.put(BaseStats.FORAGING_FORTUNE, new PlayerStat(settings.getBaseForagingFortune()));

        baseStats.put(BaseStats.MOVEMENT_SPEED, new AttributePlayerStat(playerState.getUniqueId(), Attributes.MOVEMENT_SPEED,
                plugin.getNamespacedKey("movement_speed"), 100));

        baseStats.put(BaseStats.ATTACK_SPEED, new AttributePlayerStat(playerState.getUniqueId(), Attributes.ATTACK_SPEED,
                plugin.getNamespacedKey("attack_speed")));

        if (Server.getVersion() >= Version.V1_21) {
            baseStats.put(BaseStats.MINING_SPEED, new AttributePlayerStat(playerState.getUniqueId(), Attributes.MINING_EFFICIENCY,
                    plugin.getNamespacedKey("mining_speed"), 20));
        }

        customStats.putAll(settings.getCustomStats());
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

        return (int) StatsUtil.calculateStatTotal(base, modifierLevels);
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