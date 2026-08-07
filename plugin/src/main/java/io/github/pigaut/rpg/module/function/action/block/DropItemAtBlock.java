package io.github.pigaut.rpg.module.function.action.block;

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
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class DropItemAtBlock implements Action {

    private final ItemDrop itemDrop;

    public DropItemAtBlock(@NotNull ItemDrop itemDrop) {
        this.itemDrop = itemDrop;
    }

    @Override
    public void execute(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return;
        }

        Location location = LocationUtil.centered(block.getLocation());
        itemDrop.spawn(location, context, ItemSpawnReason.ACTION);
    }

}
