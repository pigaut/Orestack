package io.github.pigaut.rpg.module.function.condition.collection;

import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class CollectionMaxTierEquals implements Condition {

    private final Amount amount;

    public CollectionMaxTierEquals(Amount amount) {
        this.amount = amount;
    }

    @Override
    public @Nullable Boolean isMet(@NotNull Context context) {
        ItemCollection collection = context.get(ItemCollection.class);
        if (collection == null) {
            return false;
        }
        return amount.match(collection.getMaxTier() + 1);
    }

}