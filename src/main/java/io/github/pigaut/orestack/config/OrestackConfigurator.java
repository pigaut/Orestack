package io.github.pigaut.orestack.config;

import dev.aurelium.auraskills.api.skill.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.action.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.hook.auraskill.*;
import io.github.pigaut.voxel.config.*;
import io.github.pigaut.voxel.core.function.action.*;
import io.github.pigaut.voxel.core.function.condition.*;
import io.github.pigaut.voxel.core.function.condition.config.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class OrestackConfigurator extends PluginConfigurator {

    public OrestackConfigurator(@NotNull OrestackPlugin plugin) {
        super(plugin);

        addLoader(GeneratorTemplate.class, new GeneratorLoader());

        final ActionLoader actions = getActionLoader();
        final ConditionLoader conditions = getConditionLoader();

        // Generator Actions
        actions.addLoader("KEEP_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorKeepStageAction());

        actions.addLoader("NEXT_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorNextStageAction());

        actions.addLoader("PREVIOUS_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorPreviousStageAction());

        actions.addLoader("SET_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorSetStageAction(line.getRequiredInteger(1) - 1));


        // AuraSkills Conditions and Actions
        boolean auraSkillsEnabled = SpigotServer.isPluginEnabled("AuraSkills");

        conditions.addLoader("HAS_AURA_LEVEL", (ConfigLoader.Line<Condition>) line -> {
            if (!auraSkillsEnabled) {
                throw new InvalidConfigurationException(line, "AuraSkills is not loaded/enabled");
            }
            return new HasAuraSkillLevel(
                    line.getRequired(1, Amount.class),
                    line.get("skill", Skills.class).withDefault(Skills.MINING)
            );
        });

        actions.addLoader("GIVE_AURA_EXP", (ConfigLoader.Line<Action>) line -> {
            if (!auraSkillsEnabled) {
                throw new InvalidConfigurationException(line, "AuraSkills is not loaded/enabled");
            }
            return new GiveAuraExp(
                    line.getRequired(1, Amount.class),
                    line.get("skill", Skills.class).withDefault(Skills.MINING),
                    line.getBoolean("raw").withDefault(false)
            );
        });

        conditions.addLoader("HAS_AURA_MANA", (ConfigLoader.Line<Condition>) line -> {
            if (!auraSkillsEnabled) {
                throw new InvalidConfigurationException(line, "AuraSkills is not loaded/enabled");
            }
            return new HasAuraMana(line.getRequired(1, Amount.class));
        });

        actions.addLoader("GIVE_AURA_MANA", (ConfigLoader.Line<Action>) line -> {
            if (!auraSkillsEnabled) {
                throw new InvalidConfigurationException(line, "AuraSkills is not loaded/enabled");
            }
            return new GiveAuraMana(line.getRequired(1, Amount.class));
        });

        actions.addLoader("TAKE_AURA_MANA", (ConfigLoader.Line<Action>) line -> {
            if (!auraSkillsEnabled) {
                throw new InvalidConfigurationException(line, "AuraSkills is not loaded/enabled");
            }
            return new TakeAuraMana(line.getRequired(1, Amount.class));
        });

    }

}
