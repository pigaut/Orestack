package io.github.pigaut.orestack.skill;

import io.github.pigaut.voxel.player.stat.*;
import io.github.pigaut.voxel.player.state.*;
import org.jetbrains.annotations.*;

public class SkillStats {

    public static final SkillStats EMPTY = SkillStats.create().build();

    private final int damage;
    private final int defense;

    private final int maxHealth;
    private final int maxMana;

    private final int miningFortune;
    private final int miningSpeed;

    private SkillStats(Builder builder) {
        this.damage = builder.damage;
        this.defense = builder.defense;
        this.maxHealth = builder.maxHealth;
        this.maxMana = builder.maxMana;
        this.miningFortune = builder.miningFortune;
        this.miningSpeed = builder.miningSpeed;
    }

    public static @NotNull Builder create() {
        return new Builder();
    }

    public void apply(@NotNull PlayerState playerState, @NotNull Skill skill) {
        String skillName = skill.getName();

        PlayerStat damageStat = playerState.getStat(StatType.DAMAGE);
        if (damageStat != null) {
            if (damage != 0) {
                damageStat.setSkillBonus(skillName, damage);
            } else {
                damageStat.removeSkillBonus(skillName);
            }
        }

        PlayerStat defenseStat = playerState.getStat(StatType.DEFENSE);
        if (defenseStat != null) {
            if (defense != 0) {
                defenseStat.setSkillBonus(skillName, defense);
            } else {
                defenseStat.removeSkillBonus(skillName);
            }
        }

        PlayerStat maxHealthStat = playerState.getStat(StatType.MAX_HEALTH);
        if (maxHealthStat != null) {
            if (maxHealth != 0) {
                maxHealthStat.setSkillBonus(skillName, maxHealth);
            } else {
                maxHealthStat.removeSkillBonus(skillName);
            }
        }

        PlayerStat maxManaStat = playerState.getStat(StatType.MAX_MANA);
        if (maxManaStat != null) {
            if (maxMana != 0) {
                maxManaStat.setSkillBonus(skillName, maxMana);
            } else {
                maxManaStat.removeSkillBonus(skillName);
            }
        }

        PlayerStat miningFortuneStat = playerState.getStat(StatType.MINING_FORTUNE);
        if (miningFortuneStat != null) {
            if (miningFortune != 0) {
                miningFortuneStat.setSkillBonus(skillName, miningFortune);
            } else {
                miningFortuneStat.removeSkillBonus(skillName);
            }
        }

        PlayerStat miningSpeedStat = playerState.getStat(StatType.MINING_SPEED);
        if (miningSpeedStat != null) {
            if (miningSpeed != 0) {
                miningSpeedStat.setSkillBonus(skillName, miningSpeed);
            } else {
                miningSpeedStat.removeSkillBonus(skillName);
            }
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMiningFortune() {
        return miningFortune;
    }

    public int getMiningSpeed() {
        return miningSpeed;
    }

    public Builder toBuilder() {
        return new Builder()
                .setDamage(damage)
                .setDefense(defense)
                .setMaxHealth(maxHealth)
                .setMaxMana(maxMana)
                .setMiningFortune(miningFortune)
                .setMiningSpeed(miningSpeed);
    }

    public @NotNull SkillStats add(@NotNull SkillStats other) {
        return create()
                .setDamage(this.damage + other.damage)
                .setDefense(this.defense + other.defense)
                .setMaxHealth(this.maxHealth + other.maxHealth)
                .setMaxMana(this.maxMana + other.maxMana)
                .setMiningFortune(this.miningFortune + other.miningFortune)
                .setMiningSpeed(this.miningSpeed + other.miningSpeed)
                .build();
    }

    public @NotNull SkillStats multiply(int factor) {
        return create()
                .setDamage(this.damage * factor)
                .setDefense(this.defense * factor)
                .setMaxHealth(this.maxHealth * factor)
                .setMaxMana(this.maxMana * factor)
                .setMiningFortune(this.miningFortune * factor)
                .setMiningSpeed(this.miningSpeed * factor)
                .build();
    }

    public SkillStats copy() {
        return toBuilder().build();
    }

    public static class Builder {

        private int damage;
        private int defense;

        private int maxHealth;
        private int maxMana;

        private int miningFortune;
        private int miningSpeed;

        private Builder() {
        }

        public Builder setDamage(int damage) {
            this.damage = damage;
            return this;
        }

        public Builder setDefense(int defense) {
            this.defense = defense;
            return this;
        }

        public Builder setMaxHealth(int maxHealth) {
            this.maxHealth = maxHealth;
            return this;
        }

        public Builder setMaxMana(int maxMana) {
            this.maxMana = maxMana;
            return this;
        }

        public Builder setMiningFortune(int miningFortune) {
            this.miningFortune = miningFortune;
            return this;
        }

        public Builder setMiningSpeed(int miningSpeed) {
            this.miningSpeed = miningSpeed;
            return this;
        }

        public SkillStats build() {
            return new SkillStats(this);
        }

    }

}