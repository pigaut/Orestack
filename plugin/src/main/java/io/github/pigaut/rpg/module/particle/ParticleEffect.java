package io.github.pigaut.rpg.module.particle;

import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface ParticleEffect extends Identifiable {

    void emit(@NotNull LivingEntity entity);

    void spawn(@NotNull Location location, @Nullable Player viewer);

}
