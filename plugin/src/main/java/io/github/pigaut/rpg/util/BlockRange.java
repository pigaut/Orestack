package io.github.pigaut.rpg.util;

import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class BlockRange {

    public static final BlockRange ZERO = new BlockRange(Amount.ZERO, Amount.ZERO, Amount.ZERO);

    public final Amount rangeX;
    public final Amount rangeY;
    public final Amount rangeZ;

    public BlockRange(@NotNull Amount rangeX, @NotNull Amount rangeY, @NotNull Amount rangeZ) {
        this.rangeX = rangeX;
        this.rangeY = rangeY;
        this.rangeZ = rangeZ;
    }

    public int getBlockX() {
        return rangeX.intValue();
    }

    public double getX() {
        return rangeX.doubleValue();
    }

    public int getBlockY() {
        return rangeY.intValue();
    }

    public double getY() {
        return rangeY.doubleValue();
    }

    public int getBlockZ() {
        return rangeZ.intValue();
    }

    public double getZ() {
        return rangeZ.doubleValue();
    }

}
