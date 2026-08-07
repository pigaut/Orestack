package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class GiveItemToPlayer implements Action {

    private final ItemDrop itemDrop;

    public GiveItemToPlayer(ItemDrop itemDrop) {
        this.itemDrop = itemDrop;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            itemDrop.give(player, context.tool(), ItemSpawnReason.ACTION);
        }
    }

}
