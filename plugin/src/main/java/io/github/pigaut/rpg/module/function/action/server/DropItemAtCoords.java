package io.github.pigaut.rpg.module.function.action.server;

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
import org.jetbrains.annotations.*;

public class DropItemAtCoords implements Action {

    private final ItemDrop itemDrop;
    private final Location location;

    public DropItemAtCoords(@NotNull ItemDrop itemDrop, @NotNull World world, double x, double y, double z) {
        this.itemDrop = itemDrop;
        this.location = LocationUtil.centered(new Location(world, x, y, z));
    }

    @Override
    public void execute(@NotNull Context context) {
        itemDrop.spawn(location, context, ItemSpawnReason.ACTION);
    }

}
