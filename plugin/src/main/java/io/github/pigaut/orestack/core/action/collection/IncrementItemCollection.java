package io.github.pigaut.orestack.core.action.collection;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.collection.template.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.action.*;
import io.github.pigaut.voxel.player.data.*;
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
        PlayerData playerData = context.playerData();
        if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
            return;
        }

        Collection collection = rpgPlayerData.getItemCollection(collectionName);
        if (collection == null) {
            return;
        }

        collection.increaseAmount(context, amount.intValue());
    }
}
