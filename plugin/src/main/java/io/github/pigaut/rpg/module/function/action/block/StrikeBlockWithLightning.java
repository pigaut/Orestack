package io.github.pigaut.rpg.module.function.action.block;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class StrikeBlockWithLightning implements BlockAction.Executor {

    private final boolean doDamage;

    public StrikeBlockWithLightning(boolean doDamage) {
        this.doDamage = doDamage;
    }

    @Override
    public void execute(@NotNull Block block) {
        World world = block.getWorld();
        Location location = block.getLocation();

        if (doDamage) {
            world.strikeLightning(location);
        } else {
            world.strikeLightningEffect(location);
        }
    }

}
