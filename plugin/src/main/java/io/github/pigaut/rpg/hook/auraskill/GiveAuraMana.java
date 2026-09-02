package io.github.pigaut.rpg.hook.auraskill;

import dev.aurelium.auraskills.api.*;
import dev.aurelium.auraskills.api.user.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class GiveAuraMana implements PlayerAction.Executor {

    private static final AuraSkillsApi AURA_SKILLS = AuraSkillsApi.get();

    private final Amount manaAmount;

    public GiveAuraMana(Amount manaAmount) {
        this.manaAmount = manaAmount;
    }

    @Override
    public void execute(@NotNull Player player) {
        SkillsUser skillsUser = AURA_SKILLS.getUser(player.getUniqueId());

        double totalMana = skillsUser.getMana() + manaAmount.doubleValue();
        skillsUser.setMana(Math.min(totalMana, skillsUser.getMaxMana()));
    }

}
