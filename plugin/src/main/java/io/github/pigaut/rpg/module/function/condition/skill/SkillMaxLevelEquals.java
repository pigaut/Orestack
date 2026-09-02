package io.github.pigaut.rpg.module.function.condition.skill;

import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class SkillMaxLevelEquals implements Condition {

    private final Amount amount;

    public SkillMaxLevelEquals(Amount amount) {
        this.amount = amount;
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Context context) {
        Skill skill = context.get(Skill.class);
        if (skill == null) {
            return new FunctionError("Context does not have a skill");
        }

        return FunctionResponse.met(amount.match(skill.getMaxLevel()));
    }

}
