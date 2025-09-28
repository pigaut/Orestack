package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.action.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.config.*;
import io.github.pigaut.voxel.core.function.action.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class OrestackConfigurator extends PluginConfigurator {

    public OrestackConfigurator(@NotNull OrestackPlugin plugin) {
        super(plugin);

        addLoader(GeneratorTemplate.class, new GeneratorLoader());

        final ActionLoader actions = this.getActionLoader();
        actions.addLoader("KEEP_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorKeepStageAction());

        actions.addLoader("NEXT_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorNextStageAction());

        actions.addLoader("PREVIOUS_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorPreviousStageAction());

        actions.addLoader("SET_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorSetStageAction(line.getRequiredInteger(1) - 1));

    }

}
