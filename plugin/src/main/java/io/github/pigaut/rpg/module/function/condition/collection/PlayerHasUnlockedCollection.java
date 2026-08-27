package io.github.pigaut.rpg.module.function.condition.collection;

import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.player.data.*;
import org.jetbrains.annotations.*;

public class PlayerHasUnlockedCollection implements RpgPlayerDataCondition {

    private final String collectionName;

    public PlayerHasUnlockedCollection(@NotNull CollectionTemplate template) {
        this.collectionName = template.getName();
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull PlayerData playerData) {
        ItemCollection collection = playerData.getItemCollection(collectionName);
        if (collection == null) {
            return false;
        }
        return collection.isUnlocked();
    }

}
