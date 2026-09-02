package io.github.pigaut.rpg.module.function.action.server;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DropExp implements Action {

    private final ExpDrop expDrop;

    public DropExp(@NotNull ExpDrop expDrop) {
        this.expDrop = expDrop;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Settings settings = context.settings();

        for (DropLocation dropLocation : settings.getItemDropLocationPriority()) {
            if (dropLocation == DropLocation.BLOCK) {
                Block block = context.block();
                if (block == null) {
                    continue;
                }

                Location location = LocationUtil.centered(block.getLocation());
                expDrop.spawn(location, context.tool());
                return FunctionResponse.NONE;
            }

            if (dropLocation == DropLocation.MOB) {
                Mob mob = context.mob();
                if (mob == null) {
                    continue;
                }
                Location location = LocationUtil.centered(mob.getLocation());
                Player player = mob.getLastDamager();
                ItemStack tool = player != null ? PlayerUtil.getTool(player) : null;
                expDrop.spawn(location, tool);
                return FunctionResponse.NONE;
            }

            if (dropLocation == DropLocation.PLAYER) {
                Player player = context.player();
                if (player == null) {
                    continue;
                }

                Location location = LocationUtil.centered(player.getLocation());
                expDrop.spawn(location, context.tool());
                return FunctionResponse.NONE;
            }
        }

        return new FunctionError("Could not find any valid exp drop location");
    }

}
