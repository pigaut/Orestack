package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.particle.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SpawnParticleAtMob implements MobAction {

    private final ParticleEffect particle;

    public SpawnParticleAtMob(ParticleEffect particle) {
        this.particle = particle;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        LivingEntity entity = mob.getEntity();
        if (entity != null) {
            particle.emit(entity);
        }
    }

}
