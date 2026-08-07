package io.github.pigaut.rpg.core.hologram;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class OffsetHologramTemplate implements HologramTemplate {

    private final HologramTemplate hologram;
    public final double offsetX, offsetY, offsetZ;

    public OffsetHologramTemplate(HologramTemplate hologram, double offsetX, double offsetY, double offsetZ) {
        this.hologram = hologram;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        Location offsetLocation = rotation.apply(location.clone(), offsetX, offsetY, offsetZ);
        return hologram.spawn(offsetLocation, context);
    }

}
