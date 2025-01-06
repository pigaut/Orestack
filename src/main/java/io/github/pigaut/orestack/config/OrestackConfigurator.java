package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.config.*;
import org.jetbrains.annotations.*;

public class OrestackConfigurator extends PluginConfigurator<OrestackPlugin> {

    public OrestackConfigurator(@NotNull OrestackPlugin plugin) {
        super(plugin);
        addLoader(Generator.class, new GeneratorLoader());
    }

}
