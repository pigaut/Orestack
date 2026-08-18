package io.github.pigaut.rpg.module.particle.type;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class BlockParticle extends AbstractParticle {

    private final BlockData blockData;

    public BlockParticle(String name, String group, Particle particle, Amount amount,
                         BlockRange range, boolean playerOnly, Material block) {
        super(name, group, particle, amount, range, playerOnly);
        blockData = block.createBlockData();
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
        return blockData;
    }

}
