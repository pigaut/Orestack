package io.github.pigaut.rpg.module.particle.type;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class NoteParticle extends AbstractParticle {

    public NoteParticle(String name, @Nullable String group,
                        Particle particle, Amount amount, boolean playerOnly, Amount note) {
        super(name, group, particle, amount, new BlockRange(note, Amount.ZERO, Amount.ZERO), playerOnly);
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
