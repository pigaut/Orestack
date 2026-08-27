package io.github.pigaut.rpg.module.gate.template;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class GateTemplateManager extends ConfigBackedManager<GateTemplate> {

    public GateTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.GATES, GateTemplate.class);
        prefix("Gate");
    }

}
