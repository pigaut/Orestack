package io.github.pigaut.rpg.module.function.condition.collection;

import io.github.pigaut.rpg.module.collection.ItemCollection;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class PlayerCollectionTierEquals implements PlayerDataCondition.Predicate {

    private final String collectionName;
    private final Amount amount;

    public PlayerCollectionTierEquals(@NotNull Amount amount, @NotNull CollectionTemplate template) {
        this.amount = amount;
        this.collectionName = template.getName();
    }

    @Override
    public boolean test(@NotNull PlayerData playerData) {
        ItemCollection collection = playerData.getItemCollection(collectionName);
        if (collection == null) {
            return false;
        }
        return amount.match(collection.getCurrentTier());
    }

}
