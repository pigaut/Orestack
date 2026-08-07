package io.github.pigaut.rpg.module.particle;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class RepeatedParticle implements ParticleEffect {

    private final ParticleEffect particle;
    private final int repetitions;

    public RepeatedParticle(ParticleEffect particle, int repetitions) {
        this.particle = particle;
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
        for (int i = 0; i < repetitions; i++) {
            particle.emit(entity);
        }
    }

    @Override
    public void spawn(@NotNull Location location, @Nullable Player viewer) {
        for (int i = 0; i < repetitions; i++) {
            particle.spawn(location, viewer);
        }
    }

}
