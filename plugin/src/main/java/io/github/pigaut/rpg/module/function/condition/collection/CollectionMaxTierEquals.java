package io.github.pigaut.rpg.module.function.condition.collection;

import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class CollectionMaxTierEquals implements Condition.Predicate {

    private final Amount amount;

    public CollectionMaxTierEquals(Amount amount) {
        this.amount = amount;
    }

    @Override
    public boolean test(@NotNull Context context) {
        ItemCollection collection = context.get(ItemCollection.class);
        if (collection == null) {
            return false;
        }
        return amount.match(collection.getMaxTier() + 1);
    }

}