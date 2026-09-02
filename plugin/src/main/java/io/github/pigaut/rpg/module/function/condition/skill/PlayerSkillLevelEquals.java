package io.github.pigaut.rpg.module.function.condition.skill;

import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.skill.template.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class PlayerSkillLevelEquals implements PlayerDataCondition {

    private final String skillName;
    private final Amount amount;

    public PlayerSkillLevelEquals(@NotNull Amount amount, @NotNull SkillTemplate skill) {
        this.skillName = skill.getName();
        this.amount = amount;
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull PlayerData playerData) {
        Skill skill = playerData.getSkill(skillName);
        if (skill == null) {
            return new FunctionError("Player does not have the skill: " + skillName);
        }
        return FunctionResponse.met(amount.match(skill.getCurrentLevel()));
    }

}
