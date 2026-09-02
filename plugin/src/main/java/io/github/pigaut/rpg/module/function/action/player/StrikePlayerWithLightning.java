package io.github.pigaut.rpg.module.function.action.player;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class StrikePlayerWithLightning implements PlayerAction.Executor {

    private final boolean doDamage;

    public StrikePlayerWithLightning(boolean doDamage) {
        this.doDamage = doDamage;
    }

    @Override
    public void execute(@NotNull Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();

        if (doDamage) {
            world.strikeLightning(location);
        }
        else {
            world.strikeLightningEffect(location);
        }
    }

}
