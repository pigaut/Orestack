package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.collection.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class CollectionConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("HAS_UNLOCKED_COLLECTION", (ConfigLoader.Line<Condition>) line ->
                new PlayerHasUnlockedCollection(line.getRequired(1, CollectionTemplate.class)));

        conditions.addLoader("HAS_COLLECTION_TIER", (ConfigLoader.Line<Condition>) line ->
                new PlayerCollectionTierEquals(line.getRequired(1, Amount.class),
                        line.getRequired("collection", CollectionTemplate.class)));

        conditions.addLoader("COLLECTION_NAME_EQUALS", (ConfigLoader.Line<Condition>) line ->
                new CollectionNameEquals(line.getRequiredString(1)));

        conditions.addLoader("COLLECTION_IS_UNLOCKED", (ConfigLoader.Line<Condition>) line ->
                new CollectionIsUnlocked());

        conditions.addLoader("COLLECTION_HAS_REWARDS", (ConfigLoader.Line<Condition>) line ->
                new CollectionHasRewards());

        conditions.addLoader("COLLECTION_TIER_EQUALS", (ConfigLoader.Line<Condition>) line ->
                new CollectionTierEquals(line.getRequired(1, Amount.class)));

        conditions.addLoader("COLLECTION_TIER_IS_IN_PROGRESS", (ConfigLoader.Line<Condition>) line ->
                new CollectionTierEquals(Amount.fixed(line.getRequiredInteger(1) - 1)));

        conditions.addLoader("COLLECTION_TIER_IS_COMPLETED", (ConfigLoader.Line<Condition>) line ->
                new CollectionTierEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));

        conditions.addLoader("COLLECTION_MAX_TIER_EQUALS", (ConfigLoader.Line<Condition>) line ->
                new CollectionMaxTierEquals(line.getRequired(1, Amount.class)));

        conditions.addLoader("COLLECTION_HAS_TIER", (ConfigLoader.Line<Condition>) line ->
                new CollectionMaxTierEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));

        conditions.addAliases("HAS_UNLOCKED_COLLECTION", "PLAYER_HAS_UNLOCKED_COLLECTION");
        conditions.addAliases("HAS_COLLECTION_TIER", "PLAYER_HAS_COLLECTION_TIER");
    }

}
