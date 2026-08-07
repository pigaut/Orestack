package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.event.drop.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DropItemAtMob implements MobAction {

    private final ItemDrop itemDrop;

    public DropItemAtMob(ItemDrop itemDrop) {
        this.itemDrop = itemDrop;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        Location location = mob.getLocation();
        if (location == null) {
            return;
        }

        location = LocationUtil.centered(location);
        Player player = mob.getLastDamager();
        ItemStack tool = player != null ? PlayerUtil.getTool(player) : null;
        itemDrop.spawn(location, player, tool, ItemSpawnReason.ACTION);
    }

}
