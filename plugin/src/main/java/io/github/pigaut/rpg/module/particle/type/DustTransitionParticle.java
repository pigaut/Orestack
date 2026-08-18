package io.github.pigaut.rpg.module.particle.type;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.lang.reflect.*;

// Instantiate in 1.20.6+ only
public class DustTransitionParticle extends AbstractParticle {

    private Object dustTransition;

    public DustTransitionParticle(String name, @Nullable String group,
                                  Particle particle, Amount amount, BlockRange range, boolean playerOnly,
                                  Color startColor, Color endColor, Amount size) {
        super(name, group, particle, amount, range, playerOnly);
        try {
            Class<?> dustTransitionClass = Class.forName("org.bukkit.Particle$DustTransition");
            Constructor<?> constructor = dustTransitionClass.getConstructor(Color.class, Color.class, float.class);
            dustTransition = constructor.newInstance(startColor, endColor, (float) size.doubleValue());
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("DustTransitionParticle can only be instantiated in 1.20.6+");
        }
        catch (Exception ignored) {
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
        return dustTransition;
    }

}
