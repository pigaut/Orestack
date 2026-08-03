package io.github.pigaut.orestack.core.condition.skill;

import io.github.pigaut.orestack.core.condition.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.orestack.skill.template.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.module.function.condition.*;
import io.github.pigaut.voxel.module.function.condition.player.data.*;
import io.github.pigaut.voxel.player.data.*;
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
    public @Nullable Boolean evaluate(@NotNull RpgPlayerData playerData) {
        Skill skill = playerData.getSkill(skillName);
        return skill != null ? amount.match(skill.getCurrentLevel()) : null;
    }

}
