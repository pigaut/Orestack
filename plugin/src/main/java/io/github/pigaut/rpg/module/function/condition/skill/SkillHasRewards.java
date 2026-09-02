package io.github.pigaut.rpg.module.function.condition.skill;

import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillHasRewards implements Condition {

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Context context) {
        Skill skill = context.get(Skill.class);
        if (skill == null) {
            return new FunctionError("Context does not have a skill");
        }

        List<String> rewards = skill.getNextLevelRewards();
        return FunctionResponse.met(rewards != null && !rewards.isEmpty());
    }

}
