package io.github.pigaut.rpg.core.transform;

import io.github.pigaut.rpg.bukkit.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class TransformUtil {

    public static Location transform(@NotNull Location location, @NotNull Transform transform) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        switch (transform.rotation()) {
            case RIGHT -> {
                int tmp = x;
                x = -z;
                z = tmp;
            }
            case BACK -> {
                x = -x;
                z = -z;
            }
            case LEFT -> {
                int tmp = x;
                x = z;
                z = -tmp;
            }
            case NONE -> {}
        }

        switch (transform.inclination()) {
            case ROLLED -> {
                // Rotate around Z axis
                int tmp = x;
                x = -y;
                y = tmp;
            }
            case UPSIDE_DOWN -> {
                y = -y;
            }
        }

        switch (transform.mirror()) {
            case LEFT_RIGHT -> x = -x;
            case FRONT_BACK -> z = -z;
            case NONE -> {}
        }

        return new Location(location.getWorld(), x, y, z);
    }


}
