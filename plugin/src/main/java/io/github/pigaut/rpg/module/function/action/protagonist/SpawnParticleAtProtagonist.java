package io.github.pigaut.rpg.module.function.action.protagonist;

import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.particle.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SpawnParticleAtProtagonist implements ProtagonistAction.Executor {

    private final ParticleEffect particle;

    public SpawnParticleAtProtagonist(ParticleEffect particle) {
        this.particle = particle;
    }

    @Override
    public void execute(@NotNull LivingEntity protagonist) {
        particle.emit(protagonist);
    }

}