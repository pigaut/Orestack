package io.github.pigaut.rpg.hook.mcmmo;

import com.gmail.nossr50.datatypes.skills.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.registry.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.registry.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class McMMOHook {

    public static void registerAllConditions(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        ConfigLoader.Line<Condition> NOT_ENABLED_CONDITION_LOADER = line -> {
            throw new InvalidConfigException(line, "McMMO plugin is not installed");
        };

        if (!Server.isPluginEnabled("McMMO")) {
            conditions.addLoader("HAS_MCMMO_LEVEL", NOT_ENABLED_CONDITION_LOADER);
            return;
        }

        conditions.addLoader("HAS_MCMMO_LEVEL", (ConfigLoader.Line<Condition>) line ->
                new HasMcMMOLevel(
                        line.getRequired(1, Amount.class),
                        line.get("skill", PrimarySkillType.class).withDefault(PrimarySkillType.MINING)
                ));

    }

    public static void registerAllActions(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        ConfigLoader.Line<Action> NOT_ENABLED_ACTION_LOADER = line -> {
            throw new InvalidConfigException(line, "McMMO plugin is not installed");
        };

        if (!Server.isPluginEnabled("McMMO")) {
            actions.addLoader("GIVE_MCMMO_EXP", NOT_ENABLED_ACTION_LOADER);
            actions.addLoader("TAKE_MCMMO_EXP", NOT_ENABLED_ACTION_LOADER);
            actions.addLoader("RESET_MCMMO_COOLDOWNS", NOT_ENABLED_ACTION_LOADER);
            actions.addLoader("LEVEL_UP_MCMMO_SKILL", NOT_ENABLED_ACTION_LOADER);
            return;
        }

        actions.addLoader("GIVE_MCMMO_EXP", (ConfigLoader.Line<Action>) line ->
                new GiveMcMMOExp(
                        line.getRequired(1, Amount.class),
                        line.get("skill", PrimarySkillType.class).withDefault(PrimarySkillType.MINING)
                ));

        actions.addLoader("TAKE_MCMMO_EXP", (ConfigLoader.Line<Action>) line ->
                new TakeMcMMOExp(
                        line.getRequired(1, Amount.class),
                        line.get("skill", PrimarySkillType.class).withDefault(PrimarySkillType.MINING)
                ));

        actions.addLoader("RESET_MCMMO_COOLDOWNS", (ConfigLoader.Line<Action>) line ->
                new ResetMcMMOCooldowns());


        actions.addLoader("LEVEL_UP_MCMMO_SKILL", (ConfigLoader.Line<Action>) line ->
                new LevelUpMcMMOSkill(line.getRequired(1, PrimarySkillType.class)));
    }

}
