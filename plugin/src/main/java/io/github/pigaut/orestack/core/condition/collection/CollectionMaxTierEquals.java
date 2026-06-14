package io.github.pigaut.orestack.core.condition.collection;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.condition.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class CollectionMaxTierEquals implements Condition {

    private final Amount amount;

    public CollectionMaxTierEquals(Amount amount) {
        this.amount = amount;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        Collection collection = context.get(Collection.class);
        if (collection == null) {
            return false;
        }
        return amount.match(collection.getMaxTier() + 1);
    }

}