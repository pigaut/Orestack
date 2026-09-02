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

public class DropExpAtMob implements MobAction.Executor {

    private final ExpDrop expDrop;

    public DropExpAtMob(@NotNull ExpDrop expDrop) {
        this.expDrop = expDrop;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        Location location = LocationUtil.centered(mob.getLocation());
        Player player = mob.getLastDamager();
        ItemStack tool = player != null ? PlayerUtil.getTool(player) : null;
        expDrop.spawn(location, tool);
    }

}
