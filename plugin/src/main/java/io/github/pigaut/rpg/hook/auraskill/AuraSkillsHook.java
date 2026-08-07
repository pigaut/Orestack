package io.github.pigaut.rpg.hook.auraskill;

import dev.aurelium.auraskills.api.skill.*;
import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.config.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.config.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;

public class AuraSkillsHook {

    public static void registerConfiguration(PluginConfigurator configurator) {
        ConditionLoader conditions = configurator.getConditionLoader();

        ConfigLoader.Line<Condition> NOT_ENABLED_CONDITION_LOADER = line -> {
            throw new InvalidConfigException(line, "AuraSkills plugin is not installed");
        };

        if (!Server.isPluginEnabled("AuraSkills")) {
            conditions.addLoader("HAS_AURA_LEVEL", NOT_ENABLED_CONDITION_LOADER);
            conditions.addLoader("HAS_AURA_MANA", NOT_ENABLED_CONDITION_LOADER);
            return;
        }

        conditions.addLoader("HAS_AURA_LEVEL", (ConfigLoader.Line<Condition>) line ->
                new HasAuraSkillLevel(
                        line.getRequired(1, Amount.class),
                        line.get("skill", Skills.class).withDefault(Skills.MINING)
                ));

        conditions.addLoader("HAS_AURA_MANA", (ConfigLoader.Line<Condition>) line ->
                new HasAuraMana(line.getRequired(1, Amount.class)));

        ActionLoader actions = configurator.getActionLoader();

        ConfigLoader.Line<Action> NOT_ENABLED_ACTION_LOADER = line -> {
            throw new InvalidConfigException(line, "AuraSkills plugin is not installed");
        };

        if (!Server.isPluginEnabled("AuraSkills")) {
            actions.addLoader("GIVE_AURA_EXP", NOT_ENABLED_ACTION_LOADER);
            actions.addLoader("GIVE_AURA_MANA", NOT_ENABLED_ACTION_LOADER);
            actions.addLoader("TAKE_AURA_MANA", NOT_ENABLED_ACTION_LOADER);
            return;
        }

        actions.addLoader("GIVE_AURA_EXP", (ConfigLoader.Line<Action>) line ->
                new GiveAuraExp(
                        line.getRequired(1, Amount.class),
                        line.get("skill", Skills.class).withDefault(Skills.MINING),
                        line.getBoolean("raw").withDefault(false)
                ));

        actions.addLoader("GIVE_AURA_MANA", (ConfigLoader.Line<Action>) line ->
                new GiveAuraMana(line.getRequired(1, Amount.class)));

        actions.addLoader("TAKE_AURA_MANA", (ConfigLoader.Line<Action>) line ->
                new TakeAuraMana(line.getRequired(1, Amount.class)));

    }

}
