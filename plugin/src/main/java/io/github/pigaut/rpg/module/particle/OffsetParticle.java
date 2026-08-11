package io.github.pigaut.rpg.module.particle;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.*;
import org.jetbrains.annotations.*;

public class OffsetParticle implements ParticleEffect {

    private final ParticleEffect particle;
    private final Amount offsetX;
    private final Amount offsetY;
    private final Amount offsetZ;

    public OffsetParticle(ParticleEffect particle, Amount offsetX, Amount offsetY, Amount offsetZ) {
        this.particle = particle;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
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
        double xOffset = offsetX.doubleValue();
        double yOffset = offsetY.doubleValue();
        double zOffset = offsetZ.doubleValue();

        Location spawnLocation = entity.getLocation();
        Vector forward = EntityUtil.getForwardVector(entity);
        Vector right = EntityUtil.getRightVector(entity);

        spawnLocation.add(forward.getX() * xOffset + right.getX() * zOffset,
                yOffset, forward.getZ() * xOffset + right.getZ() * zOffset);

        particle.spawn(spawnLocation, entity instanceof Player player ? player : null);
    }

    @Override
    public void spawn(@NotNull Location location, @Nullable Player viewer) {
        particle.spawn(location.clone().add(offsetX.doubleValue(), offsetY.doubleValue(), offsetZ.doubleValue()), viewer);
    }

}
