package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.plugin.task.*;
import io.github.pigaut.yaml.util.*;
import org.jetbrains.annotations.*;

import java.time.*;

public class GeneratorState {
    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    protected final BasicGenerator generator;

    private int currentPhase = 0;
    private @Nullable Task growthTask = null;
    private @Nullable Instant growthStart = null;
    private @Nullable Double health;

    public GeneratorState(@NotNull BasicGenerator generator) {
        this.generator = generator;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
        cancelGrowthTask();
        GeneratorPhase newPhase = generator.getPhase(currentPhase);
        if (newPhase.getState() != GrowthState.REGROWN) {
            int growthTime = newPhase.getGrowthTime();
            growthStart = Instant.now();
            growthTask = plugin.getScheduler().runTaskLater(growthTime, () -> {
                growthTask = null;
                generator.grow();
            });
        }
    }

    public @Nullable Double getHealth() {
        if (!generator.getTemplate().hasHealth()) {
            return null;
        }

        double totalHealth = 0.0;
        for (int i = 0; i < currentPhase; i++) {
            Double phaseHealth = generator.getPhase(i).getMaxHealth();
            if (phaseHealth != null) {
                totalHealth += phaseHealth;
            }
        }

        if (health != null) {
            totalHealth += health;
        }

        return totalHealth;
    }

    public @Nullable Double getPhaseHealth() {
        return health;
    }

    public void setHealth(@Nullable Double health) {
        Preconditions.checkArgument(health == null || health > 0, "Health must be greater than 0");
        this.health = health;
        generator.onStateChange();
    }

    public @Nullable Instant getGrowthStart() {
        return growthStart;
    }

    public void cancelGrowthTask() {
        growthStart = null;
        if (growthTask != null) {
            if (!growthTask.isCancelled()) {
                growthTask.cancel();
            }
            growthTask = null;
        }
    }

    public int getTicksToNextPhase() {
        if (growthStart == null) {
            return 0;
        }
        GeneratorPhase phase = generator.getPhase(currentPhase);
        int timePassed = (int) (Duration.between(growthStart, Instant.now()).toMillis() / 50);
        return phase.getGrowthTime() - timePassed;
    }

    public int getTicksToRegrownPhase() {
        GeneratorTemplate template = generator.getTemplate();
        int ticksToRegrown = getTicksToNextPhase();
        for (int i = currentPhase + 1; i < template.getMaxPhase(); i++) {
            GeneratorPhase phase = template.getPhase(i);
            if (phase.getGrowthChance() == null && phase.getGrowthTime() != 0) {
                ticksToRegrown += phase.getGrowthTime();
            }
        }
        return ticksToRegrown;
    }
}
