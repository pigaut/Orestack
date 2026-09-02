package io.github.pigaut.rpg.hook.auraskill;

import dev.aurelium.auraskills.api.*;
import dev.aurelium.auraskills.api.skill.*;
import dev.aurelium.auraskills.api.user.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class HasAuraSkillLevel implements PlayerCondition {

    private static final AuraSkillsApi AURA_SKILLS = AuraSkillsApi.get();

    private final Amount level;
    private final Skills skill;

    public HasAuraSkillLevel(@NotNull Amount level, @NotNull Skills skill) {
        this.level = level;
        this.skill = skill;
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Player player) {
        if (!skill.isEnabled()) {
            return new FunctionError("Cannot check level because skill is not enabled (AuraSkills)");
        }

        SkillsUser skillsUser = AURA_SKILLS.getUser(player.getUniqueId());
        return FunctionResponse.met(level.match(skillsUser.getSkillLevel(skill)));
    }

}
