package io.github.pigaut.rpg.module.particle;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DelayedParticle implements ParticleEffect {

    private final EnhancedPlugin plugin;
    private final ParticleEffect particle;
    private final int delay;

    public DelayedParticle(EnhancedPlugin plugin, ParticleEffect particle, int delay) {
        this.plugin = plugin;
        this.particle = particle;
        this.delay = delay;
    }

    @Override
    public @NotNull String getName() {
        return particle.getName();
    }

    @Override
    public @Nullable String getGroup() {
        return particle.getGroup();
    }

    @Override
    public void emit(@NotNull LivingEntity entity) {
        plugin.getScheduler().runTaskLater(delay, () -> particle.emit(entity));
    }

    @Override
    public void spawn(@NotNull Location location, @Nullable Player viewer) {
        plugin.getScheduler().runTaskLater(delay, () -> particle.spawn(location, viewer));
    }

}
