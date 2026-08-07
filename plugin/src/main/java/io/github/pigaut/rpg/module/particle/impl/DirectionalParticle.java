package io.github.pigaut.rpg.module.particle.impl;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class DirectionalParticle extends AbstractParticle {

    private final Amount speed;

    public DirectionalParticle(String name, @Nullable String group, Particle particle, Amount amount,
                               BlockRange direction, boolean playerOnly, Amount speed) {
        super(name, group, particle, amount, direction, playerOnly);
        this.speed = speed;
    }

    @Override
    public int getParticleCount() {
        return 0;
    }

    @Override
    public double getExtra() {
        return speed.doubleValue();
    }

    @Override
    public @Nullable Object getData() {
        return null;
    }

}
