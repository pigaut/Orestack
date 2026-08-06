package io.github.pigaut.rpg.module.collection.tier;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionTier {

    private final int amount;
    private final List<String> rewards;
    private final Function onCompletion;
    private final Function onRegression;

    public CollectionTier(int amount, @NotNull List<String> rewards,
                          @NotNull Function onCompletion, @NotNull Function onRegression) {
        this.amount = amount;
        this.rewards = rewards;
        this.onCompletion = onCompletion;
        this.onRegression = onRegression;

    }

    public int getAmountRequired() {
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
