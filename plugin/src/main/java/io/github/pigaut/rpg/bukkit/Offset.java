package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class Offset {

    public final double x;
    public final double y;
    public final double z;

    public Offset(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public @NotNull Location getOffsetLocation(@NotNull Location origin) {
        return getOffsetLocation(origin, Rotation.NONE);
    }

    public @NotNull Location getOffsetLocation(@NotNull Location origin, @NotNull Rotation rotation) {
        return rotation.apply(origin.clone(), x, y, z);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Offset offset)) return false;
        return Double.compare(x, offset.x) == 0 && Double.compare(y, offset.y) == 0 && Double.compare(z, offset.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
