package io.github.pigaut.orestack.health;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.global.*;
import org.jetbrains.annotations.*;

public class HealthUtil {

    private static @Nullable Integer toRoundedInt(@Nullable Double value) {
        return value != null ? (int) Math.round(value) : null;
    }

    public static @Nullable Integer getGeneratorHealthInt(@NotNull Generator generator) {
        return toRoundedInt(generator.getTotalHealth());
    }

    public static @Nullable Integer getGeneratorMaxHealthInt(@NotNull Generator generator) {
        return toRoundedInt(generator.getTemplate().getMaxHealth());
    }

    public static @Nullable Integer getPhaseHealthInt(@NotNull Generator generator) {
        return toRoundedInt(generator.getHealth());
    }

    public static @Nullable Integer getPhaseMaxHealthInt(@NotNull Generator generator) {
        return toRoundedInt(generator.getPhase().getMaxHealth());
    }

    public static @Nullable Integer getHealthPercentage(@NotNull Generator generator) {
        Double maxHealth = generator.getTemplate().getMaxHealth();
        Double currentHealth = generator.getState().getHealth();
        if (maxHealth == null || currentHealth == null) {
            return null;
        }

        double ratio = (maxHealth > 0) ? (currentHealth / maxHealth) : 0.0;

        int percent = (int) Math.round(ratio * 100.0);
        percent = Math.max(0, Math.min(100, percent));
        return percent;
    }

    public static @Nullable Integer getPhaseHealthPercentage(@NotNull Generator generator) {
        Double maxHealth = generator.getPhase().getMaxHealth();
        Double currentHealth = generator.getState().getPhaseHealth();
        if (maxHealth == null || currentHealth == null) {
            return null;
        }

        double ratio = (maxHealth > 0) ? (currentHealth / maxHealth) : 0.0;

        int percent = (int) Math.round(ratio * 100.0);
        percent = Math.max(0, Math.min(100, percent));
        return percent;
    }

}
