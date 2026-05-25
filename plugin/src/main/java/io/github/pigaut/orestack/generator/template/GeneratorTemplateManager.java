package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.config.*;
import io.github.pigaut.voxel.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class GeneratorTemplateManager extends ConfigBackedManager<GeneratorTemplate> {

    public GeneratorTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.GENERATORS, GeneratorTemplate.class);
        prefix("Generator");
    }

}
