package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.collection.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class CollectionActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("INCREMENT_COLLECTION", (ConfigLoader.Line<Action>) line ->
                new IncrementItemCollection(
                        line.getRequired(1, CollectionTemplate.class),
                        line.get("amount", Amount.class).withDefault(Amount.ONE)
                ));

        actions.addLoader("DECREMENT_COLLECTION", (ConfigLoader.Line<Action>) line ->
                new DecrementItemCollection(
                        line.getRequired(1, CollectionTemplate.class),
                        line.get("amount", Amount.class).withDefault(Amount.ONE)
                ));

        actions.addAliases("INCREMENT_COLLECTION", "INCREMENT_ITEM_COLLECTION", "INCREASE_COLLECTION", "INCREASE_ITEM_COLLECTION");
        actions.addAliases("DECREMENT_COLLECTION", "DECREMENT_ITEM_COLLECTION", "DECREASE_COLLECTION", "DECREASE_ITEM_COLLECTION");
    }

}
