package io.github.pigaut.orestack.core.condition.skill;

import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.module.function.condition.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class SkillLevelEquals implements Condition {

    private final Amount amount;

    public SkillLevelEquals(Amount amount) {
        this.amount = amount;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        Skill skill = context.get(Skill.class);
        if (skill == null) {
            return null;
        }
        return amount.match(skill.getCurrentLevel());
    }

}
