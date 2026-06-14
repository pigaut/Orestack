package io.github.pigaut.orestack.collection.tier;

import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionTierLoader implements ConfigLoader<CollectionTier> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid collection tier";
    }

    @Override
    public @NotNull CollectionTier loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        int amount = section.getInteger("amount")
                .requireOrThrow(Requirements.positive());

        List<String> rewards = section.getStringList("rewards", StringColor.FORMATTER)
                .orEmpty();

        Function onCompletion = section.get("on-completion", Function.class)
                .withDefault(null);

        Function onRegression = section.get("on-regression", Function.class)
                .withDefault(null);

        return new CollectionTier(amount, rewards, onCompletion, onRegression);
    }

}
