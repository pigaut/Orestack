package io.github.pigaut.rpg.module.particle.impl;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemParticle extends AbstractParticle {

    private final ItemStack item;

    public ItemParticle(String name, String group, Particle particle, Amount amount,
                        BlockRange range, boolean playerOnly, Material material) {
        super(name, group, particle, amount, range, playerOnly);
        item = new ItemStack(material);
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
        return item;
    }

}
