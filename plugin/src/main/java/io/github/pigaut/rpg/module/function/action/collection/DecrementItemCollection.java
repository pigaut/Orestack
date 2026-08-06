package io.github.pigaut.rpg.module.function.action.collection;

import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class DecrementItemCollection implements Action {

    private final String collectionName;
    private final Amount amount;

    public DecrementItemCollection(CollectionTemplate template, Amount amount) {
        this.collectionName = template.getName();
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Context context) {
        PlayerData playerData = context.playerData();
        if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
            return;
        }

        ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
        if (collection == null) {
            return;
        }

        collection.decreaseAmount(context, amount.intValue());
    }

}
