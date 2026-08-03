package io.github.pigaut.orestack.skill;

import io.github.pigaut.voxel.module.stat.*;
import io.github.pigaut.voxel.module.stat.modifier.*;
import io.github.pigaut.voxel.player.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillStats {

    public static final SkillStats EMPTY = SkillStats.create().build();

    private final Map<Stat, StatModifier> stats;

    private SkillStats(Builder builder) {
        this.stats = new HashMap<>(builder.stats);
    }

    public static @NotNull Builder create() {
        return new Builder();
    }

    public @Nullable StatModifier get(@NotNull Stat stat) {
        return stats.get(stat);
    }

    public void applyAll(@NotNull PlayerState playerState, @NotNull Skill skill) {
        removeAll(playerState, skill);

        String skillName = skill.getName();
        for (Map.Entry<Stat, StatModifier> entry : stats.entrySet()) {
            applyStat(playerState, entry.getKey(), skillName, entry.getValue());
        }
    }

    public void removeAll(@NotNull PlayerState playerState, @NotNull Skill skill) {
        String skillName = skill.getName();
        for (Stat type : stats.keySet()) {
            removeStat(playerState, type, skillName);
        }
    }

    private void applyStat(@NotNull PlayerState playerState, @NotNull Stat type,
                           @NotNull String skillName, @Nullable StatModifier modifier) {
        if (modifier == null) {
            return;
        }
        PlayerStat stat = playerState.getStat(type);
        if (stat != null) {
            stat.setSkillBonus(skillName, modifier);
        }
    }

    private void removeStat(@NotNull PlayerState playerState, @NotNull Stat type,
                            @NotNull String skillName) {
        PlayerStat stat = playerState.getStat(type);
        if (stat != null) {
            stat.removeSkillBonus(skillName);
        }
    }

    public @Nullable StatModifier getDamage() {
        return stats.get(BaseStats.DAMAGE);
    }

    public @Nullable StatModifier getDefense() {
        return stats.get(BaseStats.DEFENSE);
    }

    public @Nullable StatModifier getMaxHealth() {
        return stats.get(BaseStats.MAX_HEALTH);
    }

    public @Nullable StatModifier getMaxMana() {
        return stats.get(BaseStats.MAX_MANA);
    }

    public @Nullable StatModifier getHealthRegen() {
        return stats.get(BaseStats.HEALTH_REGEN);
    }

    public @Nullable StatModifier getManaRegen() {
        return stats.get(BaseStats.MANA_REGEN);
    }

    public @Nullable StatModifier getCritDamage() {
        return stats.get(BaseStats.CRIT_DAMAGE);
    }

    public @Nullable StatModifier getCritChance() {
        return stats.get(BaseStats.CRIT_CHANCE);
    }

    public @Nullable StatModifier getMiningFortune() {
        return stats.get(BaseStats.MINING_FORTUNE);
    }

    public @Nullable StatModifier getFarmingFortune() {
        return stats.get(BaseStats.FARMING_FORTUNE);
    }

    public @Nullable StatModifier getForagingFortune() {
        return stats.get(BaseStats.FORAGING_FORTUNE);
    }

    public @Nullable StatModifier getMovementSpeed() {
        return stats.get(BaseStats.MOVEMENT_SPEED);
    }

    public @Nullable StatModifier getAttackSpeed() {
        return stats.get(BaseStats.ATTACK_SPEED);
    }

    public @Nullable StatModifier getMiningSpeed() {
        return stats.get(BaseStats.MINING_SPEED);
    }

    public Builder toBuilder() {
        return new Builder().setAll(stats);
    }

    public SkillStats copy() {
        return toBuilder().build();
    }

    public static class Builder {

        private final Map<Stat, StatModifier> stats = new HashMap<>();

        private Builder() {
        }

        public @NotNull Builder set(@NotNull Stat stat, @Nullable StatModifier modifier) {
            if (modifier == null) {
                stats.remove(stat);
            } else {
                stats.put(stat, modifier);
            }
            return this;
        }

        private Builder setAll(@NotNull Map<Stat, StatModifier> source) {
            stats.clear();
            stats.putAll(source);
            return this;
        }

        public Builder setDamage(@Nullable StatModifier damage) {
            return set(BaseStats.DAMAGE, damage);
        }

        public Builder setDefense(@Nullable StatModifier defense) {
            return set(BaseStats.DEFENSE, defense);
        }

        public Builder setMaxHealth(@Nullable StatModifier maxHealth) {
            return set(BaseStats.MAX_HEALTH, maxHealth);
        }

        public Builder setMaxMana(@Nullable StatModifier maxMana) {
            return set(BaseStats.MAX_MANA, maxMana);
        }

        public Builder setHealthRegen(@Nullable StatModifier healthRegen) {
            return set(BaseStats.HEALTH_REGEN, healthRegen);
        }

        public Builder setManaRegen(@Nullable StatModifier manaRegen) {
            return set(BaseStats.MANA_REGEN, manaRegen);
        }

        public Builder setCritDamage(@Nullable StatModifier critDamage) {
            return set(BaseStats.CRIT_DAMAGE, critDamage);
        }

        public Builder setCritChance(@Nullable StatModifier critChance) {
            return set(BaseStats.CRIT_CHANCE, critChance);
        }

        public Builder setMiningFortune(@Nullable StatModifier miningFortune) {
            return set(BaseStats.MINING_FORTUNE, miningFortune);
        }

        public Builder setFarmingFortune(@Nullable StatModifier farmingFortune) {
            return set(BaseStats.FARMING_FORTUNE, farmingFortune);
        }

        public Builder setForagingFortune(@Nullable StatModifier foragingFortune) {
            return set(BaseStats.FORAGING_FORTUNE, foragingFortune);
        }

        public Builder setMovementSpeed(@Nullable StatModifier movementSpeed) {
            return set(BaseStats.MOVEMENT_SPEED, movementSpeed);
        }

        public Builder setAttackSpeed(@Nullable StatModifier attackSpeed) {
            return set(BaseStats.ATTACK_SPEED, attackSpeed);
        }

        public Builder setMiningSpeed(@Nullable StatModifier miningSpeed) {
            return set(BaseStats.MINING_SPEED, miningSpeed);
        }

        public SkillStats build() {
            return new SkillStats(this);
        }

    }

}