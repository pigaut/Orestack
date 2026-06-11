package io.github.pigaut.orestack.collection.tier;

import io.github.pigaut.voxel.data.function.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionTier {

    private final int amount;
    private final List<String> rewards;
    private final Function onCompletion;
    private final Function onRegression;

    public CollectionTier(int amount, List<String> rewards,
                          Function onCompletion, Function onRegression) {
        this.amount = amount;
        this.rewards = rewards;
        this.onCompletion = onCompletion;
        this.onRegression = onRegression;

    }

    public int getAmount() {
        return amount;
    }

    public @NotNull List<String> getRewards() {
        return new ArrayList<>(rewards);
    }

    public @Nullable Function getOnCompletion() {
        return onCompletion;
    }

    public @Nullable Function getOnRegression() {
        return onRegression;
    }

}
