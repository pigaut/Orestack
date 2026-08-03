package io.github.pigaut.orestack.core.condition.collection;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.collection.tier.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.module.function.condition.*;
import org.jetbrains.annotations.*;

public class CollectionHasRewards implements Condition {

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        ItemCollection collection = context.get(ItemCollection.class);
        if (collection == null) {
            return false;
        }

        CollectionTier tier = collection.getTier();
        return tier != null ? !tier.getRewards().isEmpty() : null;
    }

}