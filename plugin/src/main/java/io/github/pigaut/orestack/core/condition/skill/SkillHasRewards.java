package io.github.pigaut.orestack.core.condition.skill;

import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.orestack.skill.level.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.module.function.condition.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillHasRewards implements Condition {

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        Skill skill = context.get(Skill.class);
        if (skill == null) {
            return null;
        }

        List<String> rewards = skill.getNextLevelRewards();
        return rewards != null && !rewards.isEmpty();
    }

}
