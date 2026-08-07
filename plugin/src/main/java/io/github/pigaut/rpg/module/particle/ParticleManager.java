package io.github.pigaut.rpg.module.particle;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class ParticleManager extends ConfigBackedManager<ParticleEffect> {

    public ParticleManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.PARTICLES, ParticleEffect.class);
    }

}
