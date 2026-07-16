package io.github.pigaut.orestack.skill.config;

import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.player.stat.*;
import io.github.pigaut.voxel.player.stat.modifier.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class SkillStatsLoader implements ConfigLoader<SkillStats> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid skill stats";
    }

    @Override
    public @NotNull SkillStats loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        StatModifier damage = section.get("damage", StatModifier.class).withDefault(null);
        StatModifier defense = section.get("defense", StatModifier.class).withDefault(null);
        StatModifier maxHealth = section.get("max-health", StatModifier.class).withDefault(null);
        StatModifier maxMana = section.get("max-mana", StatModifier.class).withDefault(null);
        StatModifier healthRegen = section.get("health-regen", StatModifier.class).withDefault(null);
        StatModifier manaRegen = section.get("mana-regen", StatModifier.class).withDefault(null);
        StatModifier critDamage = section.get("crit-damage", StatModifier.class).withDefault(null);
        StatModifier critChance = section.get("crit-chance", StatModifier.class).withDefault(null);
        StatModifier miningFortune = section.get("mining-fortune", StatModifier.class).withDefault(null);
        StatModifier farmingFortune = section.get("farming-fortune", StatModifier.class).withDefault(null);
        StatModifier foragingFortune = section.get("foraging-fortune", StatModifier.class).withDefault(null);
        StatModifier movementSpeed = section.get("movement-speed", StatModifier.class).withDefault(null);
        StatModifier attackSpeed = section.get("attack-speed", StatModifier.class).withDefault(null);
        StatModifier miningSpeed = section.get("mining-speed", StatModifier.class).withDefault(null);

        return SkillStats.create()
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
                .setMiningSpeed(miningSpeed)
                .build();
    }

}