package io.github.pigaut.rpg.module.function.condition.collection;

import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.jetbrains.annotations.*;

public class CollectionNameEquals implements Condition.Predicate {

    private final String collectionName;

    public CollectionNameEquals(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public boolean test(@NotNull Context context) {
        ItemCollection collection = context.get(ItemCollection.class);
        if (collection == null) {
            return false;
        }
        return collection.getName().equals(collectionName);
    }

}