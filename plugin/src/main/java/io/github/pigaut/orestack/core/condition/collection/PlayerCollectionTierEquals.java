package io.github.pigaut.orestack.core.condition.collection;

import io.github.pigaut.orestack.collection.Collection;
import io.github.pigaut.orestack.collection.template.*;
import io.github.pigaut.orestack.core.condition.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.voxel.data.function.condition.player.data.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerCollectionTierEquals implements RpgPlayerDataCondition {

    private final String collectionName;
    private final Amount amount;

    public PlayerCollectionTierEquals(@NotNull Amount amount, @NotNull CollectionTemplate template) {
        this.amount = amount;
        this.collectionName = template.getName();
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull RpgPlayerData playerData) {
        Collection collection = playerData.getItemCollection(collectionName);
        if (collection == null) {
            return false;
        }

        return amount.match(collection.getCurrentTier());
    }

}
