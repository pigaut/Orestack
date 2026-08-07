package io.github.pigaut.rpg.module.structure.global;

import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.rpg.core.transform.Rotation;

public class GlobalBlock {

    private final BlockTemplate templateBlock;
    private final Location location;
    private final Rotation rotation;

    public GlobalBlock(@NotNull BlockTemplate templateBlock, @NotNull Location location, @NotNull Rotation rotation) {
        this.templateBlock = templateBlock;
        this.location = location;
        this.rotation = rotation;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public boolean isPlaced() {
        return templateBlock.isPlaced(location, rotation);
    }

    public void place() {
        templateBlock.place(location, rotation);
    }

    public void remove() {
        templateBlock.remove(location);
    }

}
