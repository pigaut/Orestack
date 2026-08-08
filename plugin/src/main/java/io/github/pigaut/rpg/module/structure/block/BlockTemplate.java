package io.github.pigaut.rpg.module.structure.block;

import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public interface BlockTemplate {

    BlockTemplate INVALID = new BasicBlockTemplate(Material.STONE);

    BlockTemplate AIR = new BasicBlockTemplate(Material.AIR);

    boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation);

    void remove(@NotNull Location location);

    void place(@NotNull Location location, @NotNull Rotation rotation);

}