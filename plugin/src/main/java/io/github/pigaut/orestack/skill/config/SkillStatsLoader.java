package io.github.pigaut.orestack.skill.config;

import io.github.pigaut.orestack.skill.*;
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
        int damage = section.getInteger("damage").withDefault(0);
        int defense = section.getInteger("defense").withDefault(0);
        int maxHealth = section.getInteger("max-health").withDefault(0);
        int maxMana = section.getInteger("max-mana").withDefault(0);
        int miningFortune = section.getInteger("mining-fortune").withDefault(0);
        int miningSpeed = section.getInteger("mining-speed").withDefault(0);

        return SkillStats.create()
                .setDamage(damage)
                .setDefense(defense)
                .setMaxHealth(maxHealth)
                .setMaxMana(maxMana)
                .setMiningFortune(miningFortune)
                .setMiningSpeed(miningSpeed)
                .build();
    }

}
