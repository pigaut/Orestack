package io.github.pigaut.orestack.core.placeholder;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.health.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.core.placeholder.*;
import io.github.pigaut.yaml.util.*;
import org.jetbrains.annotations.*;

public class OrestackPlaceholders {

    public static void registerAll(@NotNull OrestackPlugin plugin, @NotNull PlaceholderRegistry placeholders) {
        placeholders.register("generator", context -> {
            GeneratorTemplate generatorTemplate = context.get(GeneratorTemplate.class);
            if (generatorTemplate != null) {
                return generatorTemplate.getName();
            }

            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getName() : null;
        });

        placeholders.register("generator_phase", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getState().getCurrentPhase() : null;
        });

        placeholders.register("generator_phases", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getMaxPhase() + 1 : null;
        });

        placeholders.register("generator_state", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getPhase().getState() : null;
        });

        placeholders.register("generator_rotation", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getRotation() : null;
        });

        placeholders.register("generator_world", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getOrigin().getWorld().getName() : null;
        });

        placeholders.register("generator_x", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getOrigin().getBlockX() : null;
        });

        placeholders.register("generator_y", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getOrigin().getBlockY() : null;
        });

        placeholders.register("generator_z", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getOrigin().getBlockZ() : null;
        });

        placeholders.register("phase_timer", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.formatCompact(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("phase_timer_full", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.formatFull(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("phase_timer_hours", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toHours(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("phase_timer_minutes", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toMinutes(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("phase_timer_seconds", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toSeconds(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("generator_timer", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.formatCompact(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_timer_full", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.formatFull(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_timer_hours", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toHours(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_timer_minutes", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toMinutes(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_timer_seconds", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toSeconds(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_health", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? HealthUtil.getGeneratorHealthInt(generator) : null;
        });

        placeholders.register("generator_max_health", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? HealthUtil.getGeneratorMaxHealthInt(generator) : null;
        });

        placeholders.register("phase_health", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? HealthUtil.getPhaseHealthInt(generator) : null;
        });

        placeholders.register("phase_max_health", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? HealthUtil.getPhaseMaxHealthInt(generator) : null;
        });

        OrestackSettings settings = plugin.getSettings();
        for (HealthBar healthBar : settings.getHealthBars()) {
            placeholders.register("generator_health_bar:" + healthBar.getId(), context -> {
                Generator generator = context.get(Generator.class);
                if (generator == null) {
                    return null;
                }
                Integer healthPercentage = HealthUtil.getHealthPercentage(generator);
                if (healthPercentage == null) {
                    return null;
                }
                return healthBar.getBarByHealthPercentage(healthPercentage);
            });

            placeholders.register("phase_health_bar:" + healthBar.getId(), context -> {
                Generator generator = context.get(Generator.class);
                if (generator == null) {
                    return null;
                }
                Integer healthPercentage = HealthUtil.getPhaseHealthPercentage(generator);
                if (healthPercentage == null) {
                    return null;
                }
                return healthBar.getBarByHealthPercentage(healthPercentage);
            });
        }

    }

}
