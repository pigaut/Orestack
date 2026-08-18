package io.github.pigaut.rpg.module.particle.type;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class DustParticle extends AbstractParticle {

    private final Amount red;
    private final Amount green;
    private final Amount blue;
    private final Amount size;
    private final boolean uniform;
    private Particle.DustOptions uniformDust = null;

    public DustParticle(String name, @Nullable String group,
                        Particle particle, Amount amount, BlockRange range, boolean playerOnly,
                        Amount red, Amount green, Amount blue, Amount size, boolean uniform) {
        super(name, group, particle, amount, range, playerOnly);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.size = size;
        this.uniform = uniform;
        if (uniform) {
            uniformDust = new Particle.DustOptions(Color.fromRGB(red.intValue(), green.intValue(), blue.intValue()), (float) size.doubleValue());
        }
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
        if (uniform) {
            return uniformDust;
        }
        return new Particle.DustOptions(Color.fromRGB(red.intValue(), green.intValue(), blue.intValue()), (float) size.doubleValue());
    }

}
