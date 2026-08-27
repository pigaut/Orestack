package io.github.pigaut.rpg.module.function.condition.skill;

import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.skill.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillHasRewards implements Condition {

    @Override
    public @Nullable Boolean isMet(@NotNull Context context) {
        Skill skill = context.get(Skill.class);
        if (skill == null) {
            return null;
        }

        List<String> rewards = skill.getNextLevelRewards();
        return rewards != null && !rewards.isEmpty();
    }

}
