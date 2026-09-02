package io.github.pigaut.rpg.module.function.action.mob.drop;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.Mob;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DropItemAtMob implements MobAction.Executor {

    private final ItemDrop itemDrop;

    public DropItemAtMob(@NotNull ItemDrop itemDrop) {
        this.itemDrop = itemDrop;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        Location location = LocationUtil.centered(mob.getLocation());
        Player player = mob.getLastDamager();
        ItemStack tool = player != null ? PlayerUtil.getTool(player) : null;
        itemDrop.spawn(location, player, tool, ItemSpawnReason.ACTION);
    }

}
