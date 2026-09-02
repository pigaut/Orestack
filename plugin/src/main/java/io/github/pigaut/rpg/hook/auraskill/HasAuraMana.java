package io.github.pigaut.rpg.hook.auraskill;

import dev.aurelium.auraskills.api.*;
import dev.aurelium.auraskills.api.user.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class HasAuraMana implements PlayerCondition.Predicate {

    private static final AuraSkillsApi AURA_SKILLS = AuraSkillsApi.get();

    private final Amount manaAmount;

    public HasAuraMana(Amount manaAmount) {
        this.manaAmount = manaAmount;
    }

    @Override
    public boolean test(@NotNull Player player) {
        SkillsUser skillsUser = AURA_SKILLS.getUser(player.getUniqueId());
        return manaAmount.match(skillsUser.getMana());
    }

}
