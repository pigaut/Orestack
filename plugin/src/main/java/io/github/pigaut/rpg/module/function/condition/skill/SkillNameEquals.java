package io.github.pigaut.rpg.module.function.condition.skill;

import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.skill.*;
import org.jetbrains.annotations.*;

public class SkillNameEquals implements Condition {

    private final String skillName;

    public SkillNameEquals(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        Skill skill = context.get(Skill.class);
        if (skill == null) {
            return false;
        }
        return skill.getName().equals(skillName);
    }

}
