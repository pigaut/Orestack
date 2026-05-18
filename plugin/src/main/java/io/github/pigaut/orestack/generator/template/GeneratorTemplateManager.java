package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.config.*;
import org.jetbrains.annotations.*;

public class GeneratorTemplateManager extends ConfigBackedManager<GeneratorTemplate> {

    public GeneratorTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, GeneratorTemplate.class);
        prefix("Generator");
        directory("generators");
        strategy(LoadStrategy.SEQUENCE);
    }

}
