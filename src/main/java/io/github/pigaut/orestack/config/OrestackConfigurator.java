package io.github.pigaut.orestack.config;

import dev.aurelium.auraskills.api.skill.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.action.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.hook.auraskill.*;
import io.github.pigaut.orestack.hook.mcmmo.*;
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

        final ConditionLoader conditions = getConditionLoader();
        final ActionLoader actions = getActionLoader();

        // Generator Actions
        actions.addLoader("KEEP_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorKeepStageAction());

        actions.addLoader("NEXT_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorNextStageAction());

        actions.addLoader("PREVIOUS_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorPreviousStageAction());

        actions.addLoader("SET_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorSetStageAction(line.getRequiredInteger(1) - 1));

        AuraSkillsHook.addConditions(this);
        AuraSkillsHook.addActions(this);

        McMMOHook.addConditions(this);
        McMMOHook.addActions(this);

    }

}
