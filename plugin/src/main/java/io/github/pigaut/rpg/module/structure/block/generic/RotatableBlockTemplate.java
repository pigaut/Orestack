package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class RotatableBlockTemplate extends AbstractBlockTemplate<Rotatable> {

    private final BlockFace direction;

    public RotatableBlockTemplate(@NotNull Material type, BlockFace direction) {
        super(type, Rotatable.class);
        this.direction = direction;
    }

    @Override
    public boolean matchBlockData(@NotNull Rotatable blockData, @NotNull Rotation rotation) {
        return blockData.getRotation() == rotation.translateBlockFace(direction);
    }

    @Override
    public void updateBlockData(@NotNull Rotatable blockData, @NotNull Rotation rotation) {
        blockData.setRotation(rotation.translateBlockFace(direction));
    }

}
