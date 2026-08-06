package io.github.pigaut.rpg.module.generator.template;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class GeneratorTemplateManager extends ConfigBackedManager<GeneratorTemplate> {

    public GeneratorTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.GENERATORS, GeneratorTemplate.class);
        prefix("Generator");
    }

}
