package io.github.pigaut.rpg.module.function.condition.registry;

import com.sk89q.worldedit.util.paste.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.skill.*;
import io.github.pigaut.rpg.module.skill.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class SkillConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("HAS_SKILL_LEVEL", (ConfigLoader.Line<Condition>) line ->
                new PlayerSkillLevelEquals(line.getRequired(1, Amount.class),
                        line.getRequired("skill", SkillTemplate.class)));

        conditions.addLoader("SKILL_NAME_EQUALS", (ConfigLoader.Line<Condition>) line ->
                new SkillNameEquals(line.getRequiredString(1)));

        conditions.addLoader("SKILL_HAS_REWARDS", (ConfigLoader.Line<Condition>) line ->
                new SkillHasRewards());

        conditions.addLoader("SKILL_LEVEL_EQUALS", (ConfigLoader.Line<Condition>) line ->
                new SkillLevelEquals(line.getRequired(1, Amount.class)));

        conditions.addLoader("SKILL_LEVEL_IS_LOCKED", (ConfigLoader.Line<Condition>) line ->
                new SkillLevelEquals(Amount.lessThan(line.getRequiredInteger(1))));

        conditions.addLoader("SKILL_LEVEL_IS_IN_PROGRESS", (ConfigLoader.Line<Condition>) line ->
                new SkillLevelEquals(Amount.fixed(line.getRequiredInteger(1) - 1)));

        conditions.addLoader("SKILL_LEVEL_IS_COMPLETED", (ConfigLoader.Line<Condition>) line ->
                new SkillLevelEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));

        conditions.addLoader("SKILL_MAX_LEVEL_EQUALS", (ConfigLoader.Line<Condition>) line ->
                new SkillMaxLevelEquals(line.getRequired(1, Amount.class)));

        conditions.addLoader("SKILL_HAS_LEVEL", (ConfigLoader.Line<Condition>) line ->
                new SkillMaxLevelEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));
    }

}
