package io.github.pigaut.rpg.hook.mcmmo;

import com.gmail.nossr50.datatypes.player.*;
import com.gmail.nossr50.datatypes.skills.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class HasMcMMOLevel implements McMMOPlayerCondition.Predicate {

    private final Amount levelAmount;
    private final PrimarySkillType skill;

    public HasMcMMOLevel(Amount levelAmount, PrimarySkillType skill) {
        this.levelAmount = levelAmount;
        this.skill = skill;
    }

    @Override
    public boolean test(@NotNull McMMOPlayer player) {
        return levelAmount.match(player.getSkillLevel(skill));
    }

}
