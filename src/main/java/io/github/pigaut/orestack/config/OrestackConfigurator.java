package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.voxel.config.*;
import org.jetbrains.annotations.*;

public class OrestackConfigurator extends PluginConfigurator {

    public OrestackConfigurator(@NotNull OrestackPlugin plugin) {
        super(plugin);
        addLoader(GeneratorTemplate.class, new GeneratorLoader());
        addLoader(BlockStructure.class, new BlockStructureLoader(plugin));
        addLoader(BlockChange.class, new BlockChangeLoader());
    }

}
