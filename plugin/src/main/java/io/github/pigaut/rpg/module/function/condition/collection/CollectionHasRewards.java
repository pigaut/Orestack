package io.github.pigaut.rpg.module.function.condition.collection;

import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.collection.tier.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.collection.tier.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.jetbrains.annotations.*;

public class CollectionHasRewards implements Condition.Predicate {

    @Override
    public boolean test(@NotNull Context context) {
        ItemCollection collection = context.get(ItemCollection.class);
        if (collection == null) {
            return false;
        }

        CollectionTier tier = collection.getTier();
        if (tier == null) {
            return false;
        }

        return !tier.getRewards().isEmpty();
    }

}