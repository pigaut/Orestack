package io.github.pigaut.orestack.core.condition.skill;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.condition.*;
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
