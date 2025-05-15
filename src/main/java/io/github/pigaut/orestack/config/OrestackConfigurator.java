package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.action.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.config.*;
import io.github.pigaut.voxel.core.function.action.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.core.structure.config.*;
import io.github.pigaut.yaml.configurator.loader.*;
import org.jetbrains.annotations.*;

public class OrestackConfigurator extends PluginConfigurator {

    public OrestackConfigurator(@NotNull OrestackPlugin plugin) {
        super(plugin);

        addLoader(GeneratorTemplate.class, new GeneratorLoader());

        final ActionLoader actions = this.getActionLoader();
        actions.addLoader("RESET_GENERATOR", (BranchLoader<Action>) branch ->
                new GeneratorResetStageAction());

        actions.addLoader("NEXT_STAGE", (BranchLoader<Action>) branch ->
                new GeneratorNextStageAction());

        actions.addLoader("PREVIOUS_STAGE", (BranchLoader<Action>) branch ->
                new GeneratorPreviousStageAction());

        actions.addLoader("SET_STAGE", (BranchLoader<Action>) branch ->
                new GeneratorSetStageAction(branch.getInteger("stage", 1)));

        actions.addLoader("NEXT_GENERATOR_STAGE", (BranchLoader<Action>) branch ->
                new GeneratorNextStageAction());

        actions.addLoader("PREVIOUS_GENERATOR_STAGE", (BranchLoader<Action>) branch ->
                new GeneratorPreviousStageAction());

        actions.addLoader("SET_GENERATOR_STAGE", (BranchLoader<Action>) branch ->
                new GeneratorSetStageAction(branch.getInteger("stage", 1)));

    }

}
