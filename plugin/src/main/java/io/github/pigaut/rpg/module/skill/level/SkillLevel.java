package io.github.pigaut.rpg.module.skill.level;

import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.function.Function;
import io.github.pigaut.rpg.module.skill.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillLevel {

    private final long expRequirement;
    private final SkillStats stats;
    private final List<String> rewards;
    private final Function onCompletion;
    private final Function onRegression;

    public SkillLevel(long expRequirement, @NotNull SkillStats stats,
                      @Nullable List<String> rewards,
                      @Nullable Function onProgression, @Nullable Function onRegression) {
        this.expRequirement = expRequirement;
        this.stats = stats;
        this.rewards = rewards != null ? List.copyOf(rewards) : null;
        this.onCompletion = onProgression;
        this.onRegression = onRegression;
    }

    public long getExpRequired() {
        return expRequirement;
    }

    public @NotNull SkillStats getStats() {
        return stats;
    }

    public @Nullable List<String> getRewards() {
        return rewards != null ? new ArrayList<>(rewards) : null;
    }

    public @Nullable Function getOnCompletion() {
        return onCompletion;
    }

    public @Nullable Function getOnRegression() {
        return onRegression;
    }
}
