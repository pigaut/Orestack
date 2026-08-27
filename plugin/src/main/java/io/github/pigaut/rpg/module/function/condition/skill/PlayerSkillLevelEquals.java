package io.github.pigaut.rpg.module.function.condition.skill;

import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.skill.template.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class PlayerSkillLevelEquals implements RpgPlayerDataCondition {

    private final String skillName;
    private final Amount amount;

    public PlayerSkillLevelEquals(@NotNull Amount amount, @NotNull SkillTemplate skill) {
        this.skillName = skill.getName();
        this.amount = amount;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull PlayerData playerData) {
        Skill skill = playerData.getSkill(skillName);
        return skill != null ? amount.match(skill.getCurrentLevel()) : null;
    }

}
