package io.github.pigaut.orestack.skill.level;

import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.data.function.Function;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillLevel {

    private final int expRequirement;
    private final SkillStats stats;
    private final List<String> rewards;
    private final Function onProgression;
    private final Function onRegression;

    public SkillLevel(int expRequirement, @NotNull SkillStats stats,
                      @Nullable List<String> rewards,
                      @Nullable Function onProgression, @Nullable Function onRegression) {
        this.expRequirement = expRequirement;
        this.stats = stats;
        this.rewards = rewards != null ? List.copyOf(rewards) : null;
        this.onProgression = onProgression;
        this.onRegression = onRegression;
    }

    public int getExpRequirement() {
        return expRequirement;
    }

    public @NotNull SkillStats getStats() {
        return stats;
    }

    public @Nullable List<String> getRewards() {
        return rewards;
    }

    public @Nullable Function getOnProgression() {
        return onProgression;
    }

    public @Nullable Function getOnRegression() {
        return onRegression;
    }
}
