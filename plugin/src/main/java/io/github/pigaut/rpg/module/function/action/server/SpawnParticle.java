package io.github.pigaut.rpg.module.function.action.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.particle.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class SpawnParticle implements Action {

    private final ParticleEffect particle;
    private final Location location;

    public SpawnParticle(ParticleEffect particle, World world, double x, double y, double z) {
        this.particle = particle;
        this.location = new Location(world, x, y, z);
    }

    @Override
    public void execute(@NotNull Context context) {
        particle.spawn(location, context.player());
    }

}
