package io.github.pigaut.rpg.module.function.action.block;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.particle.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SpawnParticleAtBlock implements Action.Executor {

    private final ParticleEffect particle;

    public SpawnParticleAtBlock(ParticleEffect particle) {
        this.particle = particle;
    }

    @Override
    public void execute(@NotNull Context context) {
        Block block = context.block();
        if (block != null) {
            particle.spawn(block.getLocation().add(0.5, 0.5, 0.5), context.player());
        }
    }

}
