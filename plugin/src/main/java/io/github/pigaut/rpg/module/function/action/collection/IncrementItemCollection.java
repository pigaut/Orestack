package io.github.pigaut.rpg.module.function.action.collection;

import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.player.data.base.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class IncrementItemCollection implements Action {

    private final String collectionName;
    private final Amount amount;

    public IncrementItemCollection(@NotNull CollectionTemplate template, @NotNull Amount amount) {
        this.collectionName = template.getName();
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Context context) {
        EnhancedPlayerData playerData = context.playerData();
        if (!(playerData instanceof PlayerData rpgPlayerData)) {
            return;
        }

        ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
        if (collection == null) {
            return;
        }

        collection.increaseAmount(context, amount.intValue());
    }
}
