package io.github.pigaut.orestack.skill;

import io.github.pigaut.voxel.player.stat.*;
import io.github.pigaut.voxel.player.state.*;
import org.jetbrains.annotations.*;

public class SkillStats {

    public static final SkillStats EMPTY = SkillStats.create().build();

    private final @Nullable StatModifier damage;
    private final @Nullable StatModifier defense;

    private final @Nullable StatModifier maxHealth;
    private final @Nullable StatModifier maxMana;

    private final @Nullable StatModifier healthRegen;
    private final @Nullable StatModifier manaRegen;

    private final @Nullable StatModifier critDamage;
    private final @Nullable StatModifier critChance;

    private final @Nullable StatModifier miningFortune;
    private final @Nullable StatModifier farmingFortune;
    private final @Nullable StatModifier foragingFortune;

    private final @Nullable StatModifier movementSpeed;
    private final @Nullable StatModifier attackSpeed;
    private final @Nullable StatModifier miningSpeed;

    private SkillStats(Builder builder) {
        this.damage = builder.damage;
        this.defense = builder.defense;
        this.maxHealth = builder.maxHealth;
        this.maxMana = builder.maxMana;
        this.healthRegen = builder.healthRegen;
        this.manaRegen = builder.manaRegen;
        this.critDamage = builder.critDamage;
        this.critChance = builder.critChance;
        this.miningFortune = builder.miningFortune;
        this.farmingFortune = builder.farmingFortune;
        this.foragingFortune = builder.foragingFortune;
        this.movementSpeed = builder.movementSpeed;
        this.attackSpeed = builder.attackSpeed;
        this.miningSpeed = builder.miningSpeed;
    }

    public static @NotNull Builder create() {
        return new Builder();
    }

    public void applyAll(@NotNull PlayerState playerState, @NotNull Skill skill) {
        removeAll(playerState, skill);

        String skillName = skill.getName();
        applyStat(playerState, StatType.DAMAGE, skillName, damage);
        applyStat(playerState, StatType.DEFENSE, skillName, defense);
        applyStat(playerState, StatType.MAX_HEALTH, skillName, maxHealth);
        applyStat(playerState, StatType.MAX_MANA, skillName, maxMana);
        applyStat(playerState, StatType.HEALTH_REGEN, skillName, healthRegen);
        applyStat(playerState, StatType.MANA_REGEN, skillName, manaRegen);
        applyStat(playerState, StatType.CRIT_DAMAGE, skillName, critDamage);
        applyStat(playerState, StatType.CRIT_CHANCE, skillName, critChance);
        applyStat(playerState, StatType.MINING_FORTUNE, skillName, miningFortune);
        applyStat(playerState, StatType.FARMING_FORTUNE, skillName, farmingFortune);
        applyStat(playerState, StatType.FORAGING_FORTUNE, skillName, foragingFortune);
        applyStat(playerState, StatType.MOVEMENT_SPEED, skillName, movementSpeed);
        applyStat(playerState, StatType.ATTACK_SPEED, skillName, attackSpeed);
        applyStat(playerState, StatType.MINING_SPEED, skillName, miningSpeed);
    }

    public void removeAll(@NotNull PlayerState playerState, @NotNull Skill skill) {
        String skillName = skill.getName();

        removeStat(playerState, StatType.DAMAGE, skillName);
        removeStat(playerState, StatType.DEFENSE, skillName);
        removeStat(playerState, StatType.MAX_HEALTH, skillName);
        removeStat(playerState, StatType.MAX_MANA, skillName);
        removeStat(playerState, StatType.HEALTH_REGEN, skillName);
        removeStat(playerState, StatType.MANA_REGEN, skillName);
        removeStat(playerState, StatType.CRIT_DAMAGE, skillName);
        removeStat(playerState, StatType.CRIT_CHANCE, skillName);
        removeStat(playerState, StatType.MINING_FORTUNE, skillName);
        removeStat(playerState, StatType.FARMING_FORTUNE, skillName);
        removeStat(playerState, StatType.FORAGING_FORTUNE, skillName);
        removeStat(playerState, StatType.MOVEMENT_SPEED, skillName);
        removeStat(playerState, StatType.ATTACK_SPEED, skillName);
        removeStat(playerState, StatType.MINING_SPEED, skillName);
    }

    private void applyStat(@NotNull PlayerState playerState, @NotNull StatType type,
                           @NotNull String skillName, @Nullable StatModifier modifier) {
        if (modifier == null) {
            return;
        }
        PlayerStat stat = playerState.getStat(type);
        if (stat != null) {
            stat.setSkillBonus(skillName, modifier);
        }
    }

    private void removeStat(@NotNull PlayerState playerState, @NotNull StatType type,
                            @NotNull String skillName) {
        PlayerStat stat = playerState.getStat(type);
        if (stat != null) {
            stat.removeSkillBonus(skillName);
        }
    }

    public @Nullable StatModifier getDamage() {
        return damage;
    }

    public @Nullable StatModifier getDefense() {
        return defense;
    }

    public @Nullable StatModifier getMaxHealth() {
        return maxHealth;
    }

    public @Nullable StatModifier getMaxMana() {
        return maxMana;
    }

    public @Nullable StatModifier getHealthRegen() {
        return healthRegen;
    }

    public @Nullable StatModifier getManaRegen() {
        return manaRegen;
    }

    public @Nullable StatModifier getCritDamage() {
        return critDamage;
    }

    public @Nullable StatModifier getCritChance() {
        return critChance;
    }

    public @Nullable StatModifier getMiningFortune() {
        return miningFortune;
    }

    public @Nullable StatModifier getFarmingFortune() {
        return farmingFortune;
    }

    public @Nullable StatModifier getForagingFortune() {
        return foragingFortune;
    }

    public @Nullable StatModifier getMovementSpeed() {
        return movementSpeed;
    }

    public @Nullable StatModifier getAttackSpeed() {
        return attackSpeed;
    }

    public @Nullable StatModifier getMiningSpeed() {
        return miningSpeed;
    }

    public Builder toBuilder() {
        return new Builder()
                .setDamage(damage)
                .setDefense(defense)
                .setMaxHealth(maxHealth)
                .setMaxMana(maxMana)
                .setHealthRegen(healthRegen)
                .setManaRegen(manaRegen)
                .setCritDamage(critDamage)
                .setCritChance(critChance)
                .setMiningFortune(miningFortune)
                .setFarmingFortune(farmingFortune)
                .setForagingFortune(foragingFortune)
                .setMovementSpeed(movementSpeed)
                .setAttackSpeed(attackSpeed)
                .setMiningSpeed(miningSpeed);
    }

    public SkillStats copy() {
        return toBuilder().build();
    }

    public static class Builder {

        private @Nullable StatModifier damage;
        private @Nullable StatModifier defense;

        private @Nullable StatModifier maxHealth;
        private @Nullable StatModifier maxMana;

        private @Nullable StatModifier healthRegen;
        private @Nullable StatModifier manaRegen;

        private @Nullable StatModifier critDamage;
        private @Nullable StatModifier critChance;

        private @Nullable StatModifier miningFortune;
        private @Nullable StatModifier farmingFortune;
        private @Nullable StatModifier foragingFortune;

        private @Nullable StatModifier movementSpeed;
        private @Nullable StatModifier attackSpeed;
        private @Nullable StatModifier miningSpeed;

        private Builder() {
        }

        public Builder setDamage(@Nullable StatModifier damage) {
            this.damage = damage;
            return this;
        }

        public Builder setDefense(@Nullable StatModifier defense) {
            this.defense = defense;
            return this;
        }

        public Builder setMaxHealth(@Nullable StatModifier maxHealth) {
            this.maxHealth = maxHealth;
            return this;
        }

        public Builder setMaxMana(@Nullable StatModifier maxMana) {
            this.maxMana = maxMana;
            return this;
        }

        public Builder setHealthRegen(@Nullable StatModifier healthRegen) {
            this.healthRegen = healthRegen;
            return this;
        }

        public Builder setManaRegen(@Nullable StatModifier manaRegen) {
            this.manaRegen = manaRegen;
            return this;
        }

        public Builder setCritDamage(@Nullable StatModifier critDamage) {
            this.critDamage = critDamage;
            return this;
        }

        public Builder setCritChance(@Nullable StatModifier critChance) {
            this.critChance = critChance;
            return this;
        }

        public Builder setMiningFortune(@Nullable StatModifier miningFortune) {
            this.miningFortune = miningFortune;
            return this;
        }

        public Builder setFarmingFortune(@Nullable StatModifier farmingFortune) {
            this.farmingFortune = farmingFortune;
            return this;
        }

        public Builder setForagingFortune(@Nullable StatModifier foragingFortune) {
            this.foragingFortune = foragingFortune;
            return this;
        }

        public Builder setMovementSpeed(@Nullable StatModifier movementSpeed) {
            this.movementSpeed = movementSpeed;
            return this;
        }

        public Builder setAttackSpeed(@Nullable StatModifier attackSpeed) {
            this.attackSpeed = attackSpeed;
            return this;
        }

        public Builder setMiningSpeed(@Nullable StatModifier miningSpeed) {
            this.miningSpeed = miningSpeed;
            return this;
        }

        public SkillStats build() {
            return new SkillStats(this);
        }

    }

}