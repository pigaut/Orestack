package io.github.pigaut.orestack.core.condition.collection;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.collection.template.*;
import io.github.pigaut.orestack.core.condition.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.voxel.data.function.condition.player.data.*;
import io.github.pigaut.voxel.player.data.*;
import org.jetbrains.annotations.*;

public class PlayerHasUnlockedCollection implements RpgPlayerDataCondition {

    private final String collectionName;

    public PlayerHasUnlockedCollection(@NotNull CollectionTemplate template) {
        this.collectionName = template.getName();
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull RpgPlayerData playerData) {
        Collection collection = playerData.getItemCollection(collectionName);
        if (collection == null) {
            return false;
        }
        return collection.isUnlocked();
    }

}
