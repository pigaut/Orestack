package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class DirectionalBlockTemplate extends AbstractBlockTemplate<Directional> {

    private final BlockFace direction;

    public DirectionalBlockTemplate(@NotNull Material type, @NotNull BlockFace direction) {
        super(type, Directional.class);
        this.direction = direction;
    }

    @Override
    public boolean matchBlockData(@NotNull Directional blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(direction);
    }

    @Override
    public void updateBlockData(@NotNull Directional blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(direction));
    }
}
