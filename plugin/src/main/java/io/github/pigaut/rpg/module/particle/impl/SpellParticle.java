package io.github.pigaut.rpg.module.particle.impl;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class SpellParticle extends AbstractParticle {

    private final Amount red;
    private final Amount green;
    private final Amount blue;
    private final boolean uniform;
    private Color uniformColor = null;

    public SpellParticle(String name, String group, Particle particle,
                         Amount amount, BlockRange range, boolean playerOnly,
                         Amount red, Amount green, Amount blue, boolean uniform) {
        super(name, group, particle, amount, range, playerOnly);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.uniform = uniform;
        if (uniform) {
            uniformColor = Color.fromRGB(red.intValue(), green.intValue(), blue.intValue());
        }
    }

    @Override
    public int getParticleCount() {
        return 1;
    }

    @Override
    public double getExtra() {
        return 1.0;
    }

    @Override
    public @Nullable Object getData() {
        if (uniform) {
            return uniformColor;
        }
        return Color.fromRGB(red.intValue(), green.intValue(), blue.intValue());
    }

    // Spell particle <1.20.6
    public static class Legacy extends AbstractParticle {
        public Legacy(String name, @Nullable String group, Particle particle,
                                   Amount amount, boolean playerOnly, Amount red, Amount green, Amount blue) {
            super(name, group, particle, amount, new BlockRange(red, green, blue), playerOnly);
        }

        @Override
        public int getParticleCount() {
            return 0;
        }

        @Override
        public double getExtra() {
            return 1.0;
        }

        @Override
        public @Nullable Object getData() {
            return null;
        }

    }

}
