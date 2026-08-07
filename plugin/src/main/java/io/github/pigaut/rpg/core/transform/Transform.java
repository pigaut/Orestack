package io.github.pigaut.rpg.core.transform;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public record Transform(Rotate rotation, Inclination inclination, Mirror mirror) {

    public @NotNull Location apply(@NotNull Location pivot, @NotNull Offset offset) {
        double dx = offset.getX();
        double dy = offset.getY();
        double dz = offset.getZ();

        double tempX = dx;
        double tempZ = dz;

        switch (rotation) {
            case NONE -> {
            }
            case RIGHT -> {
                dx = -tempZ;
                dz = tempX;
            }
            case BACK -> {
                dx = -tempX;
                dz = -tempZ;
            }
            case LEFT -> {
                dx = tempZ;
                dz = -tempX;
            }
        }

        switch (inclination) {
            case UPRIGHT -> {
            }
            case ROLLED -> {
                dy = -dy;
                dx = -dx;
            }
            case UPSIDE_DOWN -> {
                dy = -dy;
                dz = -dz;
            }
        }

        switch (this.mirror) {
            case NONE -> {}
            case LEFT_RIGHT -> dx = -dx;
            case FRONT_BACK -> dz = -dz;
        }

        return pivot.clone().add(dx, dy, dz);
    }

    public @NotNull BlockFace apply(@NotNull BlockFace facing) {


        return facing;
    }

}
