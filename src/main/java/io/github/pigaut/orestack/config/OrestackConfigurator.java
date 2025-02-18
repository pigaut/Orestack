package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.action.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.voxel.config.*;
import io.github.pigaut.voxel.function.action.*;
import io.github.pigaut.yaml.configurator.loader.*;
import org.jetbrains.annotations.*;

public class OrestackConfigurator extends PluginConfigurator {

    public OrestackConfigurator(@NotNull OrestackPlugin plugin) {
        super(plugin);
        addLoader(GeneratorTemplate.class, new GeneratorLoader());
        addLoader(BlockStructure.class, new BlockStructureLoader(plugin));
        addLoader(BlockChange.class, new BlockChangeLoader());

        final ActionLoader actions = this.getActionLoader();
        actions.addLoader("NEXT_STAGE", (BranchLoader<Action>) branch ->
                new NextStageAction());

        actions.addLoader("PREVIOUS_STAGE", (BranchLoader<Action>) branch ->
                new PreviousStageAction());

        actions.addLoader("SET_STAGE", (BranchLoader<Action>) branch ->
                new SetStageAction(branch.getInteger("stage", 1)));

    }

}
