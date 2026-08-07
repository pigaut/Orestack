package io.github.pigaut.rpg.module.particle;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PeriodicParticle implements ParticleEffect {

    private final EnhancedPlugin plugin;
    private final ParticleEffect particle;
    private final int interval;
    private final int repetitions;

    public PeriodicParticle(EnhancedPlugin plugin, ParticleEffect particle, int interval, int repetitions) {
        this.plugin = plugin;
        this.particle = particle;
        this.interval = interval;
        this.repetitions = repetitions;
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
        particle.emit(entity);
        for (int i = 1; i < repetitions + 1; i++) {
            final long delay = (long) interval * i;
            plugin.getScheduler().runTaskLater(delay, () -> particle.emit(entity));
        }
    }

    @Override
    public void spawn(@NotNull Location location, @Nullable Player viewer) {
        particle.spawn(location, viewer);
        for (int i = 1; i < repetitions + 1; i++) {
            final long delay = (long) interval * i;
            plugin.getScheduler().runTaskLater(delay, () -> particle.spawn(location, viewer));
        }
    }

}
