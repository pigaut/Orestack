package io.github.pigaut.rpg.module.particle.type;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GenericParticle extends AbstractParticle {

    public GenericParticle(String name, String group, Particle particle,
                           Amount amount, BlockRange range, boolean playerOnly) {
        super(name, group, particle, amount, range, playerOnly);
    }

    @Override
    public int getParticleCount() {
        return 1;
    }

    @Override
    public double getExtra() {
        return 0;
    }

    @Override
    public @Nullable Object getData() {
        return null;
    }

}
