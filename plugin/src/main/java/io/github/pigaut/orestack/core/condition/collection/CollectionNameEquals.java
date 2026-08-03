package io.github.pigaut.orestack.core.condition.collection;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.module.function.condition.*;
import org.jetbrains.annotations.*;

public class CollectionNameEquals implements Condition {

    private final String collectionName;

    public CollectionNameEquals(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        ItemCollection collection = context.get(ItemCollection.class);
        if (collection == null) {
            return false;
        }
        return collection.getName().equals(collectionName);
    }

}