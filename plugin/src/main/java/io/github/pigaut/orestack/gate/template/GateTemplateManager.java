package io.github.pigaut.orestack.gate.template;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.plugin.manager.config.*;
import io.github.pigaut.voxel.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class GateTemplateManager extends ConfigBackedManager<GateTemplate> {

    public GateTemplateManager(@NotNull OrestackPlugin plugin) {
        super(plugin, Module.GATES, GateTemplate.class);
        prefix("Gate");
    }

}
