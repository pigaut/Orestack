package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DropItemAtPlayer implements Action.Executor {

    private final ItemDrop itemDrop;

    public DropItemAtPlayer(ItemDrop itemDrop) {
        this.itemDrop = itemDrop;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            Location location = LocationUtil.centered(player.getLocation());
            itemDrop.spawn(location, context, ItemSpawnReason.ACTION);
        }
    }

}
