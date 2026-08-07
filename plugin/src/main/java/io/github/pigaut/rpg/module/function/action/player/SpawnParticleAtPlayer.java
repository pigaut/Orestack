package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.particle.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SpawnParticleAtPlayer implements PlayerAction {

    private final ParticleEffect particle;

    public SpawnParticleAtPlayer(ParticleEffect particle) {
        this.particle = particle;
    }

    @Override
    public void execute(@NotNull Player player) {
        particle.emit(player);
    }

}
