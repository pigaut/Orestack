package io.github.pigaut.rpg.module.function.action.server;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import org.bukkit.*;

public class StrikeLightning implements ServerAction.Executor {

    private final Location location;
    private final boolean doDamage;

    public StrikeLightning(World world, double x, double y, double z, boolean doDamage) {
        this.location = new Location(world, x, y, z);
        this.doDamage = doDamage;
    }

    @Override
    public void execute() {
        World world = LocationUtil.getWorldOrDefault(location);
        if (doDamage) {
            world.strikeLightning(location);
        } else {
            world.strikeLightningEffect(location);
        }
    }

}
