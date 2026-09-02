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

public class DropItemAtTopDamager implements MobAction.Executor {

    private final ItemDrop itemDrop;

    public DropItemAtTopDamager(ItemDrop itemDrop) {
        this.itemDrop = itemDrop;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        Player player = mob.getTopDamager();
        if (player == null) {
            return;
        }

        Location location = LocationUtil.centered(player.getLocation());
        ItemStack tool = PlayerUtil.getTool(player);
        itemDrop.spawn(location, player, tool, ItemSpawnReason.ACTION);
    }

}
