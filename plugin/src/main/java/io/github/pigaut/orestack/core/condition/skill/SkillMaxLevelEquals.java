package io.github.pigaut.orestack.core.condition.skill;

import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.condition.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class SkillMaxLevelEquals implements Condition {

    private final Amount amount;

    public SkillMaxLevelEquals(Amount amount) {
        this.amount = amount;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        Skill skill = context.get(Skill.class);
        if (skill == null) {
            return null;
        }

        return amount.match(skill.getMaxLevel());
    }

}
